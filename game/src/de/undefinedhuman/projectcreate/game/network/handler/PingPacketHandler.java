package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.ping.PingPacket;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class PingPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof PingPacket)) return false;
        PingPacket pingPacket = (PingPacket) packet;
        Log.info("PING RECEIVED - " + (System.currentTimeMillis() - pingPacket.time) + "ms");
        return true;
    }
}
