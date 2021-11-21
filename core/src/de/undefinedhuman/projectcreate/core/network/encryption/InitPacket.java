package de.undefinedhuman.projectcreate.core.network.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class InitPacket implements Packet {

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }
}
