package de.undefinedhuman.projectcreate.core.network.packets;

import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class PingPacket implements Packet {
    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
