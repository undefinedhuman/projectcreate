package de.undefinedhuman.projectcreate.kamino.event.events.base;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class EntityEvent extends Event {

    @Metadata
    public final long blueprintID;
    public final Entity entity;

    public EntityEvent(long blueprintID, Entity entity) {
        this.blueprintID = blueprintID;
        this.entity = entity;
    }

}
