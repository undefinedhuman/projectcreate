package de.undefinedhuman.projectcreate.engine.ecs.events;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class ComponentEvent extends Event {

    public final Entity entity;

    public ComponentEvent(Entity entity) {
        this.entity = entity;
    }

}
