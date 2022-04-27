package de.undefinedhuman.projectcreate.kamino.event.events.base;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;


public class WorldEntityEvent extends EntityEvent {

    @Metadata
    public final String worldName;

    public WorldEntityEvent(long blueprintID, Entity entity, String worldName) {
        super(blueprintID, entity);
        this.worldName = worldName;
    }
}
