package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;

public interface PacketHandler {
    void handle(Connection connection, LoginPacket packet);
    default void handle(Connection connection, CreateEntityPacket packet) {}
    default void handle(Connection connection, RemoveEntityPacket packet) {}
    void handle(Connection connection, ComponentPacket packet);
    default void handle(Connection connection, MovementPacket packet) {}
    default void handle(Connection connection, PositionPacket packet) {}
    default void handle(Connection connection, JumpPacket packet) {}
    default void handle(Connection connection, MousePacket packet) {}
    default void handle(Connection connection, SelectorPacket packet) {}
}
