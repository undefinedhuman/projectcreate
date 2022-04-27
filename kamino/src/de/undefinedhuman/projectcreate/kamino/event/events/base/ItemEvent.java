package de.undefinedhuman.projectcreate.kamino.event.events.base;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;


public class ItemEvent extends Event {

    @Metadata
    public final int itemID;

    public ItemEvent(int itemID) {
        this.itemID = itemID;
    }
}
