package de.undefinedhuman.projectcreate.core.network.packets.inventory;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.inventory.SlotInfo;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class SelectItemPacket implements Packet {

    public long interactedEntityID;
    public String inventoryName;
    public int row, col;
    public boolean half;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static SelectItemPacket serialize(SlotInfo info, boolean half) {
        SelectItemPacket selectItemPacket = new SelectItemPacket();
        selectItemPacket.interactedEntityID = info.getLinkedEntityID();
        selectItemPacket.inventoryName = info.getLinkInventoryName();
        selectItemPacket.row = info.getRow();
        selectItemPacket.col = info.getCol();
        selectItemPacket.half = half;
        return selectItemPacket;
    }

}
