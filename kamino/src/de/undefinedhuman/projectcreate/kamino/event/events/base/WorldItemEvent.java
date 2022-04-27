package de.undefinedhuman.projectcreate.kamino.event.events.base;

import com.playprojectcreate.kaminoapi.annotations.Metadata;


public class WorldItemEvent extends ItemEvent {

    @Metadata
    public final String worldName;

    public WorldItemEvent(int itemID, String worldName) {
        super(itemID);
        this.worldName = worldName;
    }

}
