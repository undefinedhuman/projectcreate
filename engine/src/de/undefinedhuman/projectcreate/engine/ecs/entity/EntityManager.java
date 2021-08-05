package de.undefinedhuman.projectcreate.engine.ecs.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

import java.util.Arrays;

public class EntityManager extends Manager {

    private static volatile EntityManager instance;

    private Engine engine;

    private EntityManager() {
        this.engine = new Engine();
    }

    @Override
    public void update(float delta) {
        engine.update(delta);
    }

    @Override
    public void delete() {
        engine.removeAllEntities();
    }

    public void addSystems(EntitySystem... systems) {
        Arrays.stream(systems).forEach(entitySystem -> engine.addSystem(entitySystem));
    }

    public Engine getEngine() {
        return engine;
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
