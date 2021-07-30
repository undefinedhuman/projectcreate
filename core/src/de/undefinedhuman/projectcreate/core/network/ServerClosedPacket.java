package de.undefinedhuman.projectcreate.core.network;

public class ServerClosedPacket implements Packet {
    public int reason;

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
