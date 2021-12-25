package de.undefinedhuman.projectcreate.server.network;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.PacketListener;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.ping.PingPacket;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.server.network.handler.PingPacketHandler;
import de.undefinedhuman.projectcreate.server.network.handler.ServerEncryptionPacketHandler;

public class ServerListener extends PacketListener {

    private static volatile ServerListener instance;

    private ServerListener() {
        super();
        registerPacketHandlers(PingPacket.class, new PingPacketHandler());
        registerPacketHandlers(EncryptionPacket.class, new ServerEncryptionPacketHandler());
    }

    @Override
    public void disconnected(Connection connection) {
        if(!(connection instanceof PlayerConnection))
            return;
        PlayerConnection playerConnection = (PlayerConnection) connection;
        if(playerConnection.worldID == -1) return;
        // ServerManager.getInstance().sendToAllExceptTCP(connection.getID(), RemoveEntityPacket.serialize(playerConnection.worldID));
        EntityManager.getInstance().removeEntity(playerConnection.worldID);
    }

    public static ServerListener getInstance() {
        if(instance != null)
            return instance;
        synchronized (ServerListener.class) {
            if (instance == null)
                instance = new ServerListener();
        }
        return instance;
    }

}
