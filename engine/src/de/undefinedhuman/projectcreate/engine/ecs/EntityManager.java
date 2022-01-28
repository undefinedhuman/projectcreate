package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.Collection;

public class EntityManager extends Manager {

    private static volatile EntityManager instance;

    private final EntityList entityList = new EntityList();
    private final SystemManager systemManager = new SystemManager();
    private final FamilyManager familyManager = new FamilyManager(entityList);
    private final Array<EntityEvent> entityEvents = new Array<>(false, 16);
    private boolean updating = false;

    private EntityManager() {
        // this.entityList.subscribe(new EntityListListener(familyManager));
    }

    @Override
    public void update(float delta) {
        if (updating)
            throw new IllegalStateException("Error calling update method of entity manager twice.");
        updating = true;
        try {
            processSystems(systemManager.getOrderedUnmodifiableSystems(), delta);
            processEvents(entityEvents);
        } finally {
            entityEvents.clear();
            updating = false;
        }
    }

    @Override
    public void delete() {
        systemManager.removeAllSystems();
        entityList.removeAllEntities();
    }

    public void addEntity(Entity entity) {
        this.addEntity(entity.getWorldID(), entity);
    }

    public void addEntity(long worldID, Entity entity) {
        entity.setWorldID(worldID);
        if (updating)
            entityEvents.add(() -> entityList.addEntity(entity));
        else entityList.addEntity(entity);
    }

    public void removeEntity(long worldID) {
        if (updating) entityEvents.add(() -> {
            Entity entity = entityList.getEntity(worldID);
            if(entity == null)
                return;
            entity.scheduledForRemoval = true;
            entityList.removeEntity(worldID);
        });
        else entityList.removeEntity(worldID);
    }

    public Collection<Entity> getEntities() {
        return entityList.getUnmodifiableEntities();
    }

    public EntityManager addSystem(System... systems) {
        for(System system : systems) {
            systemManager.addSystem(system);
            familyManager.addFamily(system.getFamily());
            system.init(familyManager);
        }
        return this;
    }

    public void removeSystem(Class<? extends System> type) {
        System system = systemManager.getSystem(type);
        if(system == null)
            return;
        systemManager.removeSystem(type);
        familyManager.removeFamily(system.getFamily());
    }

    private void processSystems(ImmutableArray<System> orderedSystems, float delta) {
        for(System system : orderedSystems)
            if(system.checkProcessing())
                system.update(delta);
    }

    private void processEvents(Array<EntityEvent> entityEvents) {
        if (entityEvents.size == 0)
            return;
        entityEvents.forEach(EntityEvent::handle);
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

    @FunctionalInterface
    interface EntityEvent {
        void handle();
    }

/*    static class EntityListListener implements EntityListener {

        private FamilyManager familyManager;
        private Observer<Entity> componentListener;

        EntityListListener(FamilyManager familyManager) {
            this.familyManager = familyManager;
            this.componentListener = familyManager::updateFamiliesForEntity;
        }

        @Override
        public void handle(Type type, Entity... entities) {
            switch (type) {
                case ADD: familyManager.addEntities(entities);
                case REMOVE: familyManager.removeEntities(entities);
                case REMOVE_ALL: familyManager.removeAllEntities();
            }
            Arrays.stream(entities).forEach(entity -> {
                Observable<Entity> observable = entity.getComponentObservable();
                if(type == Type.ADD) observable.subscribe(componentListener);
                else observable.unsubscribe(componentListener);
            });
        }
    }*/

}
