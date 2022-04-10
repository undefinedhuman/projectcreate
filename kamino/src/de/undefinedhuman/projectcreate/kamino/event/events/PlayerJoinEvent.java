package de.undefinedhuman.projectcreate.kamino.event.events;

import de.undefinedhuman.projectcreate.engine.event.Event;
import com.playprojectcreate.kaminoapi.annotations.Metadata;

public class PlayerJoinEvent extends Event {

    @Metadata()
    public final String playerID;

    public PlayerJoinEvent(String playerID) {
        this.playerID = playerID;
    }
}
