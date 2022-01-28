package de.undefinedhuman.projectcreate.engine.ecs.observer;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.observer.Event;

public class ComponentEventType extends Event<ComponentEventType.Type, Entity> {

    public ComponentEventType() {
        super(Type.class, Entity.class);
    }

    public enum Type {
        COMPONENT
    }

}
