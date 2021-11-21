package de.undefinedhuman.projectcreate.core.network.packets.inventory;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class UpdateSlotsPacket implements Packet {

    public long entityID1;
    public String inventoryName1;
    public int row1, col1, itemID1, amount1;

    public int itemID2, amount2;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static UpdateSlotsPacket serialize(long interactedEntityID, String inventoryName, int row, int col, int id1, int amount1, int selectedID, int selectedAmount) {
        UpdateSlotsPacket slotsPacket = new UpdateSlotsPacket();
        slotsPacket.entityID1 = interactedEntityID;
        slotsPacket.inventoryName1 = inventoryName;
        slotsPacket.row1 = row;
        slotsPacket.col1 = col;
        slotsPacket.itemID1 = id1;
        slotsPacket.amount1 = amount1;

        slotsPacket.itemID2 = selectedID;
        slotsPacket.amount2 = selectedAmount;
        return slotsPacket;
    }

}
