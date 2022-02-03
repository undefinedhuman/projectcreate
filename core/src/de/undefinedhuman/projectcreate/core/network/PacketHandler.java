package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;

@FunctionalInterface
public interface PacketHandler<T extends Packet> {
    void handle(Connection connection, T t);
}
