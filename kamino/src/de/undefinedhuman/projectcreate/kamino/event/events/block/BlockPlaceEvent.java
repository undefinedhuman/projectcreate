package de.undefinedhuman.projectcreate.kamino.event.events.block;

import com.badlogic.gdx.math.Vector2;
import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import com.playprojectcreate.kaminoapi.metadata.container.area.AreaMetadataContainer;
import de.undefinedhuman.projectcreate.kamino.event.events.base.WorldItemEvent;

@KaminoEvent
public class BlockPlaceEvent extends WorldItemEvent {

    @Metadata(databaseName = "area", containerType = AreaMetadataContainer.class)
    public final Vector2 position;
    @Metadata
    public final String playerUUID;

    public BlockPlaceEvent(int itemID, String worldName, Vector2 position, String playerUUID) {
        super(itemID, worldName);
        this.position = position;
        this.playerUUID = playerUUID;
    }

}
