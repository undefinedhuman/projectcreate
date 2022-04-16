package de.undefinedhuman.projectcreate.kamino.event.events;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.event.Event;

@KaminoEvent
public class EntityEvent extends Event {

    @Metadata()
    public final long entityID;
    public final Entity entity;

    public EntityEvent(long entityID, Entity entity) {
        this.entityID = entityID;
        this.entity = entity;
    }
}
