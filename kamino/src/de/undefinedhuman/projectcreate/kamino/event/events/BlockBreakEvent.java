package de.undefinedhuman.projectcreate.kamino.event.events;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class BlockBreakEvent extends Event {

    @Metadata
    public final int blockID;
    @Metadata
    public final String worldName;
    @Metadata
    public final Vector2 position;

    public BlockBreakEvent(int blockID, String worldName, Vector2 position) {
        this.blockID = blockID;
        this.worldName = worldName;
        this.position = position;
    }

}
