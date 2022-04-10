package de.undefinedhuman.projectcreate.kamino.event.events;

import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.event.Event;
import com.playprojectcreate.kaminoapi.annotations.Metadata;

public class EntityEvent extends Event {

    @Metadata()
    public final long entityID;
    public final Entity entity;

    public EntityEvent(long entityID, Entity entity) {
        this.entityID = entityID;
        this.entity = entity;
    }
}
