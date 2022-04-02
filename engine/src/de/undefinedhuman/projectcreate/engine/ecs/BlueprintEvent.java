package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.event.Event;

public class BlueprintEvent extends Event {

    public final Type type;
    public final Blueprint[] blueprint;

    public BlueprintEvent(Type type, Blueprint... blueprint) {
        this.type = type;
        this.blueprint = blueprint;
    }

    public enum Type {
        ADD,
        REMOVE
    }

}
