package de.undefinedhuman.projectcreate.kamino.event.events;

import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class PlayerJoinEvent extends Event {

    @Metadata
    public final String playerID;

    public PlayerJoinEvent(String playerID) {
        this.playerID = playerID;
    }
}
