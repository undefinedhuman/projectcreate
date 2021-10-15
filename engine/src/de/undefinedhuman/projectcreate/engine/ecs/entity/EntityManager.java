package de.undefinedhuman.projectcreate.engine.ecs.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class EntityManager extends Manager {

    private static volatile EntityManager instance;

    private HashMap<Long, Entity> entitiesByIDs = new HashMap<>();
    private ArrayList<Long> entitiesForRemoval = new ArrayList<>();
    private Engine engine;

    private EntityManager() {
        this.engine = new Engine();
    }

    @Override
    public void update(float delta) {
        removeEntities();
        engine.update(delta);
    }

    @Override
    public void delete() {
        engine.removeAllEntities();
        entitiesByIDs.clear();
    }

    public void addSystems(EntitySystem... systems) {
        Arrays.stream(systems).forEach(entitySystem -> engine.addSystem(entitySystem));
    }

    public <T extends EntitySystem> T getSystem(Class<T> systemClass) {
        return engine.getSystem(systemClass);
    }

    public void addEntity(long worldID, Entity entity) {
        entitiesByIDs.put(worldID, entity);
        this.engine.addEntity(entity);
    }

    public void removeEntity(long worldID) {
        if(!hasEntity(worldID))
            return;
        entitiesForRemoval.add(worldID);
    }

    public void removeEntities() {
        for(long worldID : entitiesForRemoval) {
            Entity entity = this.entitiesByIDs.remove(worldID);
            if(entity != null) engine.removeEntity(entity);
        }
    }

    public Stream<Map.Entry<Long, Entity>> stream() {
        return entitiesByIDs.entrySet().stream();
    }

    public boolean isRemoving(long worldID) {
        return entitiesForRemoval.contains(worldID);
    }

    public Entity getEntity(long worldID) {
        return entitiesByIDs.get(worldID);
    }

    public boolean hasEntity(long worldID) {
        return entitiesByIDs.containsKey(worldID);
    }

    public void addEntityListener(EntityListener listener) {
        this.engine.addEntityListener(listener);
    }

    public static EntityManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (EntityManager.class) {
            if (instance == null)
                instance = new EntityManager();
        }
        return instance;
    }
}
