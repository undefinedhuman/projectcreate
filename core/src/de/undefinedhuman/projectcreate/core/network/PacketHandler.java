package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;

@FunctionalInterface
public interface PacketHandler {
    boolean handle(Connection connection, Packet packet);
}
