package de.undefinedhuman.projectcreate.kamino.event.events.base;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;


public class PlayerEvent extends EntityEvent {

    @Metadata
    public final String playerUUID;

    public PlayerEvent(Entity player, String playerUUID) {
        super(0, player);
        this.playerUUID = playerUUID;
    }

}
