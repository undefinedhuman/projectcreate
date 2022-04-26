package de.undefinedhuman.projectcreate.kamino.event.events.inventory;

import com.badlogic.gdx.math.Vector2;
import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import com.playprojectcreate.kaminoapi.metadata.container.area.AreaMetadataContainer;
import de.undefinedhuman.projectcreate.kamino.event.events.base.ItemEvent;

@KaminoEvent
public class InventoryEvent extends ItemEvent {

    @Metadata(databaseName = "area", containerType = AreaMetadataContainer.class)
    public final Vector2 position;
    @Metadata
    public final String inventoryID;
    @Metadata
    public final String playerUUID;

    public InventoryEvent(int itemID, Vector2 position, String inventoryID, String playerUUID) {
        super(itemID);
        this.position = position;
        this.inventoryID = inventoryID;
        this.playerUUID = playerUUID;
    }

}
