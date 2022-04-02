package de.undefinedhuman.projectcreate.engine.ecs.events;

import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class ComponentBlueprintEvent extends Event {

    public final Type type;
    public final ComponentBlueprint<?>[] componentBlueprints;

    public ComponentBlueprintEvent(Type type, ComponentBlueprint<?>... componentBlueprints) {
        this.type = type;
        this.componentBlueprints = componentBlueprints;
    }

    public enum Type {
        ADD,
        REMOVE,
        UPDATE
    }

}
