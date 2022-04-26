package de.undefinedhuman.projectcreate.kamino.event.events.player;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.kamino.event.events.base.PlayerEvent;

@KaminoEvent
public class PlayerQuitEvent extends PlayerEvent {
    public PlayerQuitEvent(Entity player, String uuid) {
        super(player, uuid);
    }
}
