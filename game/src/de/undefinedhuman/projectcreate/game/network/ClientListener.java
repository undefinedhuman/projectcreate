package de.undefinedhuman.projectcreate.game.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.core.network.Packet;

public class ClientListener extends Listener {

    private ClientPacketHandler packetHandler;

    public ClientListener() {
        this.packetHandler = new ClientPacketHandler();
    }

    public void received(Connection connection, Object object) {
        if(!(object instanceof Packet))
            return;
        Packet packet = (Packet) object;
        packet.handle(packetHandler);
    }

}
