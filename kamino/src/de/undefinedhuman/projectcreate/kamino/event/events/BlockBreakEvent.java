package de.undefinedhuman.projectcreate.kamino.event.events;

import com.badlogic.gdx.math.Vector2;
import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import com.playprojectcreate.kaminoapi.metadata.container.area.AreaMetadataContainer;
import de.undefinedhuman.projectcreate.engine.event.Event;

@KaminoEvent
public class BlockBreakEvent extends Event {

    @Metadata
    public final int blockID;
    @Metadata
    public final String worldName;
    @Metadata(databaseName = "area", containerType = AreaMetadataContainer.class)
    public final Vector2 position;

    public BlockBreakEvent(int blockID, String worldName, Vector2 position) {
        this.blockID = blockID;
        this.worldName = worldName;
        this.position = position;
    }

}