package de.undefinedhuman.projectcreate.server.network;

import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.PingPacket;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class ServerHandler implements PacketHandler {
    @Override
    public void handle(PingPacket packet) {
        Log.info("PING RECEIVED!");
    }
}
