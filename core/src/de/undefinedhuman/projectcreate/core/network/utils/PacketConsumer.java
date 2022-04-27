package de.undefinedhuman.projectcreate.core.network.utils;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;

@FunctionalInterface
public interface PacketConsumer<P extends Packet> {
    void accept(Connection connection, P packet);
}
