package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.server.ServerManager;

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
        packet.handle(connection, packetHandler);
    }

    @Override
    public void disconnected(Connection connection) {
        if(!(connection instanceof PlayerConnection))
            return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        if(playerConnection.worldID == -1) return;
        EntityManager.getInstance().removeEntity(playerConnection.worldID);
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), RemoveEntityPacket.serialize(playerConnection.worldID));
    }
}
