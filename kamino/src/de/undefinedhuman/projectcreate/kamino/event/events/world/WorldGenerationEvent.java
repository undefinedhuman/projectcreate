package de.undefinedhuman.projectcreate.kamino.event.events.world;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

@KaminoEvent
public class WorldGenerationEvent extends Event {

    @Metadata
    public final String worldName;

    public WorldGenerationEvent(String worldName) {
        this.worldName = worldName;
    }

}
