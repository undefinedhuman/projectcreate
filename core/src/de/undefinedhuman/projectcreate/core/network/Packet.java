package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;

public interface Packet {
    void handle(Connection connection, PacketHandler handler);
}
