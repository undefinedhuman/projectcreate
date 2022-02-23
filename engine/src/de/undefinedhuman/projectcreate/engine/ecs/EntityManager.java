package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.observer.Event;
import de.undefinedhuman.projectcreate.engine.observer.SynchronizedEventManager;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.Arrays;
import java.util.Collection;

public class EntityManager extends SynchronizedEventManager implements Manager {

    private static volatile EntityManager instance;

    private final EntityList entityList = new EntityList();
    private final FamilyManager familyManager = new FamilyManager(this);
    private final SystemManager systemManager = new SystemManager();
    private boolean updating = false;

    private EntityManager() {
        subscribe(EntityEvent.class, EntityEvent.Type.ADD, entities -> {
            entityList.addEntities(entities);
            familyManager.addEntities(entities);
        });
        subscribe(EntityEvent.class, EntityEvent.Type.REMOVE, entities -> {
            familyManager.removeEntities(entities);
            entityList.removeEntities(entities);
        });
        subscribe(Entity.ComponentEvent.class, Entity.ComponentEvent.Type.COMPONENT, familyManager::updateFamiliesForEntity);
    }

    @Override
    public void update(float delta) {
        if (updating)
            throw new IllegalStateException("Error calling update method of entity manager twice.");
        updating = true;
        try {
            systemManager.update(delta);
            processTemporaryObserverData();
        } finally {
            updating = false;
            clearTemporaryObserverData();
        }
    }

    @Override
    public void delete() {
        systemManager.removeAllSystems();
        familyManager.removeAllEntities();
        entityList.removeAllEntities();
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public Entity createEntity(int blueprintID, long worldID, int flags) {
        Entity entity = this.createEntity(blueprintID, worldID);
        entity.flags = flags;
        return entity;
    }

    public Entity createEntity(int blueprintID, long worldID) {
        Blueprint blueprint = BlueprintManager.getInstance().getBlueprint(blueprintID);
        if(blueprint == null) {
            Log.warn("Blueprint with ID " + blueprintID + " does not exist!");
            return null;
        }
        return createEntity(blueprint, worldID);
    }

    public Entity createEntity(Blueprint blueprint, long worldID) {
        return blueprint.createInstance(worldID);
    }

    public void addEntity(Entity... entities) {
        for(Entity entity : entities)
            entity.setEventManager(this);
        notify(EntityEvent.class, EntityEvent.Type.ADD, entities);
    }

    public void removeEntity(long... worldIDs) {
        Entity[] entitiesToRemove = Arrays.stream(worldIDs).mapToObj(entityList::getEntity).filter(entity -> {
            if(entity == null || entity.isScheduledForRemoval()) return false;
            entity.setEventManager(null);
            entity.scheduledForRemoval = true;
            return true;
        }).toArray(Entity[]::new);
        notify(EntityEvent.class, EntityEvent.Type.REMOVE, entitiesToRemove);
    }

    public void removeAllEntities() {
        removeEntity(entityList.getUnmodifiableEntities().stream().mapToLong(Entity::getWorldID).toArray());
    }

    public Entity getEntity(long worldID) {
        Entity entity = entityList.getEntity(worldID);
        if(entity == null)
            Log.warn("No entity with world id " + worldID + " was found!");
        return entity;
    }

    public boolean hasEntity(long worldID) {
        return entityList.hasEntity(worldID);
    }

    public Collection<Entity> getEntities() {
        return entityList.getUnmodifiableEntities();
    }

    public ImmutableArray<Entity> getEntitiesFor(Family family) {
        return familyManager.getEntitiesFor(family);
    }

    public void addSystems(System... systems) {
        for(System system : systems) {
            systemManager.addSystem(system);
            system.init(this);
        }
    }

    public void removeSystem(Class<? extends System> type) {
        System system = systemManager.getSystem(type);
        if(system == null)
            return;
        systemManager.removeSystem(type);
        system.delete(this);
    }

    public static EntityManager getInstance() {
        if (instance != null)
            return instance;
        synchronized (EntityManager.class) {
            if (instance == null)
                instance = new EntityManager();
        }
        return instance;
    }

    public static class EntityEvent extends Event<EntityEvent.Type, Entity[]> {
        public EntityEvent() {
            super(Type.class, Entity[].class);
        }
        public enum Type {
            ADD,
            REMOVE
        }
    }

    @Override
    public <EventType, DataType> void notify(Class<? extends Event<EventType, DataType>> eventClass, EventType eventType, DataType data) {
        super.notify(eventClass, eventType, data, updating);
    }
}
