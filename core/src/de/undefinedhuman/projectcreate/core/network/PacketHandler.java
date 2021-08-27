package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;

public interface PacketHandler {
    void handle(Connection connection, LoginPacket packet);
    void handle(Connection connection, CreateEntityPacket packet);
    void handle(Connection connection, RemoveEntityPacket packet);
    void handle(Connection connection, ComponentPacket packet);
    void handle(Connection connection, MovementPacket packet);
    void handle(Connection connection, PositionPacket packet);
}
