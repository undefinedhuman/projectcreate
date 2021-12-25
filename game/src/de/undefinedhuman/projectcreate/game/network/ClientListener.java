package de.undefinedhuman.projectcreate.game.network;

import de.undefinedhuman.projectcreate.core.network.PacketListener;
import de.undefinedhuman.projectcreate.core.network.packets.encryption.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.ping.PingPacket;
import de.undefinedhuman.projectcreate.game.network.handler.ClientEncryptionPacketHandler;
import de.undefinedhuman.projectcreate.game.network.handler.PingPacketHandler;

public class ClientListener extends PacketListener {

    private static volatile ClientListener instance;

    private ClientListener() {
        registerPacketHandlers(PingPacket.class, new PingPacketHandler());
        registerPacketHandlers(EncryptionPacket.class, new ClientEncryptionPacketHandler());
    }

    public static ClientListener getInstance() {
        if(instance != null)
            return instance;
        synchronized (ClientListener.class) {
            if (instance == null)
                instance = new ClientListener();
        }
        return instance;
    }

}
