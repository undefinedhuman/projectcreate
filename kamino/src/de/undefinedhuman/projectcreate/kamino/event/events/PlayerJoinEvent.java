package de.undefinedhuman.projectcreate.kamino.event.events;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

@KaminoEvent
public class PlayerJoinEvent extends Event {

    @Metadata()
    public final String playerID;

    public PlayerJoinEvent(String playerID) {
        this.playerID = playerID;
    }
}
