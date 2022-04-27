package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.ecs.events.ComponentEvent;
import de.undefinedhuman.projectcreate.engine.ecs.events.EntityEvent;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.event.SynchronizedEventManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
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
        subscribe(EntityEvent.class, entityEvent -> {
            switch (entityEvent.type) {
                case ADD -> {
                    entityList.addEntities(entityEvent.entities);
                    familyManager.addEntities(entityEvent.entities);
                }
                case REMOVE -> {
                    familyManager.removeEntities(entityEvent.entities);
                    entityList.removeEntities(entityEvent.entities);
                }
            }
        });
        subscribe(ComponentEvent.class, componentEvent -> familyManager.updateFamiliesForEntity(componentEvent.entity));
    }

    @Override
    public void update(float delta) {
        if (updating)
            throw new IllegalStateException("Error calling update method of entity manager twice.");
        updating = true;
        try {
            systemManager.update(delta);
            processEventQueue();
        } finally {
            updating = false;
            processEventQueue();
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
        notify(new EntityEvent(EntityEvent.Type.ADD, entities));
    }

    public void removeEntity(long... worldIDs) {
        Entity[] entitiesToRemove = Arrays.stream(worldIDs).mapToObj(entityList::getEntity).filter(entity -> {
            if(entity == null || entity.isScheduledForRemoval()) return false;
            entity.setEventManager(null);
            entity.scheduledForRemoval = true;
            return true;
        }).toArray(Entity[]::new);
        notify(new EntityEvent(EntityEvent.Type.REMOVE, entitiesToRemove));
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

    @Override
    public <E extends Event> void notify(E event) {
        super.notify(event, updating);
    }
}
