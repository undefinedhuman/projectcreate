package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginResponse;
import de.undefinedhuman.projectcreate.core.network.encryption.*;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginRequest;
import de.undefinedhuman.projectcreate.core.network.packets.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementRequest;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementResponse;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.SelectItemPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.UpdateSlotsPacket;

public interface PacketHandler {
    default void handle(Connection connection, LoginRequest packet) {}
    default void handle(Connection connection, CreateEntityPacket packet) {}
    default void handle(Connection connection, RemoveEntityPacket packet) {}
    void handle(Connection connection, ComponentPacket packet);
    default void handle(Connection connection, MovementRequest packet) {}
    default void handle(Connection connection, MovementResponse packet) {}
    default void handle(Connection connection, PositionPacket packet) {}
    default void handle(Connection connection, JumpPacket packet) {}
    default void handle(Connection connection, MousePacket packet) {}
    default void handle(Connection connection, SelectorPacket packet) {}
    default void handle(Connection connection, SelectItemPacket packet) {}
    default void handle(Connection connection, UpdateSlotsPacket packet) {}
    default void handle(Connection connection, InitPacket packet) {}
    default void handle(Connection connection, EncryptionRequest packet) {}
    default void handle(Connection connection, EncryptionResponse packet) {}
    default void handle(Connection connection, SessionPacket packet) {}
    default void handle(Connection connection, LoginResponse packet) {}
}
