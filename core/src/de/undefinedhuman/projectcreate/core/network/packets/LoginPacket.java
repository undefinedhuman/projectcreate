package de.undefinedhuman.projectcreate.core.network.packets;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class LoginPacket implements Packet {

    public String name;
    public long worldID;
    public String componentData;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }
}
