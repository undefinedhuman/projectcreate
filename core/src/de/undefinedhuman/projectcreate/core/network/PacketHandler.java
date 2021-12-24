package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;

public interface PacketHandler<P extends Packet> {
    void handle(Connection connection, P packet);
}
