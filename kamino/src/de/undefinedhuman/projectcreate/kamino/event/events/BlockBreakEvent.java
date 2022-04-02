package de.undefinedhuman.projectcreate.kamino.event.events;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.event.annotations.Metadata;

public class BlockBreakEvent extends Event {

    @Metadata
    public final int blockID;
    @Metadata
    public final String worldName;
    public final Vector2 position;

    public BlockBreakEvent(int blockID, String worldName, Vector2 position) {
        this.blockID = blockID;
        this.worldName = worldName;
        this.position = position;
    }

}
