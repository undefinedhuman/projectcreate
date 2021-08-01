package de.undefinedhuman.projectcreate.core.network;

import de.undefinedhuman.projectcreate.core.network.packets.PingPacket;

public interface PacketHandler {
    void handle(PingPacket packet);
}
