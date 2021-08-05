package de.undefinedhuman.projectcreate.core.network;

import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;

public interface PacketHandler {
    void handle(LoginPacket packet);
}
