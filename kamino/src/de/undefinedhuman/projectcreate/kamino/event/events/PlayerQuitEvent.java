package de.undefinedhuman.projectcreate.kamino.event.events;

import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class PlayerQuitEvent extends Event {

    @Metadata
    public final String playerID;

    public PlayerQuitEvent(String playerID) {
        this.playerID = playerID;
    }
}
