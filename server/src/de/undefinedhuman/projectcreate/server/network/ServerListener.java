package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.ping.PingPacket;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.server.ServerManager;

import java.util.HashMap;

public class ServerListener extends Listener {

    private static volatile ServerManager instance;

    private HashMap<Class<? extends Packet>, PacketHandler<?>> handlers;

    private ServerListener() {
        handlers = new HashMap<>();
        handlers.put(PingPacket.class, new PingPacketHandler());
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
        ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), RemoveEntityPacket.serialize(playerConnection.worldID));
        EntityManager.getInstance().removeEntity(playerConnection.worldID);
    }

    public static ServerManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (ServerManager.class) {
            if (instance == null)
                instance = new ServerManager();
        }
        return instance;
    }

}
