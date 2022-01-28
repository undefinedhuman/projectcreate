package de.undefinedhuman.projectcreate.engine.ecs.observer;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.observer.Event;

public class EntityEvent extends Event<EntityEvent.Type, Entity> {

    protected EntityEvent() {
        super(Type.class, Entity.class);
    }

    public enum Type {
        ADD,
        REMOVE,
        REMOVE_ALL
    }

}
