package de.undefinedhuman.projectcreate.engine.ecs.events;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class EntityEvent extends Event {

    public final Type type;
    public final Entity[] entities;

    public EntityEvent(Type type, Entity... entities) {
        this.type = type;
        this.entities = entities;
    }

    public enum Type {
        ADD,
        REMOVE
    }

}
