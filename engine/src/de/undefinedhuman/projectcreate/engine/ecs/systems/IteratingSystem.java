package de.undefinedhuman.projectcreate.engine.ecs.systems;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.System;

public abstract class IteratingSystem extends System {

    public IteratingSystem() {
        super();
    }

    public IteratingSystem(int priority) {
        super(priority);
    }

    @Override
    protected void process(float delta) {
        if(entities == null) return;
        for(Entity entity : entities)
            processEntity(delta, entity);
    }

    public abstract void processEntity(float delta, Entity entity);

}
