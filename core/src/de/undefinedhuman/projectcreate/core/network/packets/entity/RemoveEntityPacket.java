package de.undefinedhuman.projectcreate.core.network.packets.entity;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class RemoveEntityPacket implements Packet {

    public long worldID;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static RemoveEntityPacket serialize(long worldID) {
        RemoveEntityPacket packet = new RemoveEntityPacket();
        packet.worldID = worldID;
        return packet;
    }

}
