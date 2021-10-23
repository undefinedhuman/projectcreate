package de.undefinedhuman.projectcreate.core.network.packets.inventory;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class AddItemPacket implements Packet {

    public long worldID;
    public int itemID, itemAmount;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }
}
