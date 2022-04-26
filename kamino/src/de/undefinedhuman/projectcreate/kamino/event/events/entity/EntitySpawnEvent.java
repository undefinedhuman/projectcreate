package de.undefinedhuman.projectcreate.kamino.event.events.entity;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.kamino.event.events.base.WorldEntityEvent;

@KaminoEvent
public class EntitySpawnEvent extends WorldEntityEvent {
    public EntitySpawnEvent(long blueprintID, Entity entity, String worldName) {
        super(blueprintID, entity, worldName);
    }
}
