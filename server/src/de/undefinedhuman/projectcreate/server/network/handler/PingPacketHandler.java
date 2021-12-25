package de.undefinedhuman.projectcreate.server.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.ping.PingPacket;

public class PingPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof PingPacket)) return false;
        connection.sendTCP(packet);
        return true;
    }
}
