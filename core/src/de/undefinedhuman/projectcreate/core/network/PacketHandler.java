package de.undefinedhuman.projectcreate.core.network;

public interface PacketHandler {
    void handle(LoginPacket packet);
    void handle(ServerClosedPacket packet);
}
