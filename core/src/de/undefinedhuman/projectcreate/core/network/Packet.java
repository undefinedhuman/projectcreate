package de.undefinedhuman.projectcreate.core.network;

public interface Packet {
    void handle(PacketHandler handler);
}
