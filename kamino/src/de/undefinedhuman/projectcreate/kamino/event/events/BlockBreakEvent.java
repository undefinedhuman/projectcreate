package de.undefinedhuman.projectcreate.kamino.event.events;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;
import de.undefinedhuman.projectcreate.kamino.event.metadata.container.area.AreaMetadataContainer;

public class BlockBreakEvent extends Event {

    @Metadata
    public final int blockID;
    @Metadata
    public final String worldName;
    @Metadata(containerType = AreaMetadataContainer.class)
    public final Vector2 position;

    public BlockBreakEvent(int blockID, String worldName, Vector2 position) {
        this.blockID = blockID;
        this.worldName = worldName;
        this.position = position;
    }

}
