package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;

public interface PacketHandler {
    void handle(Connection connection, LoginPacket packet);
    void handle(Connection connection, CreateEntityPacket packet);
    void handle(Connection connection, RemoveEntityPacket packet);
}
