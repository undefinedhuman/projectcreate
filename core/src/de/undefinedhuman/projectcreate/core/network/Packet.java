package de.undefinedhuman.projectcreate.core.network;

interface Packet {
    void handle(PacketHandler handler);
}
