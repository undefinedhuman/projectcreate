package de.undefinedhuman.projectcreate.core.network.packets.inventory;

import com.esotericsoftware.kryonet.Connection;
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

    public static SelectItemPacket serialize(long interactedEntityID, String inventoryName, int row, int col, boolean half) {
        SelectItemPacket selectItemPacket = new SelectItemPacket();
        selectItemPacket.interactedEntityID = interactedEntityID;
        selectItemPacket.inventoryName = inventoryName;
        selectItemPacket.row = row;
        selectItemPacket.col = col;
        selectItemPacket.half = half;
        return selectItemPacket;
    }

}
