package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.core.network.Packet;

public class ServerListener extends Listener {

    private ServerPacketHandler packetHandler;

    public ServerListener() {
        packetHandler = new ServerPacketHandler();
    }

    @Override
    public void received(Connection connection, Object object) {
        if(!(object instanceof Packet))
            return;
        Packet packet = (Packet) object;
        packet.handle(packetHandler);
    }

}
