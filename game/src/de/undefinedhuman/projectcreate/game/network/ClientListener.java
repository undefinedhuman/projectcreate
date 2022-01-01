package de.undefinedhuman.projectcreate.game.network;

import de.undefinedhuman.projectcreate.core.network.PacketListener;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.game.network.handler.*;

public class ClientListener extends PacketListener {

    private static volatile ClientListener instance;

    private ClientListener() {
        registerPacketHandlers(EncryptionPacket.class, new ClientEncryptionPacketHandler());
        registerPacketHandlers(CreateEntityPacket.class, new CreateEntityPacketHandler());
        registerPacketHandlers(RemoveEntityPacket.class, new RemoveEntityPacketHandler());
        registerPacketHandlers(ComponentPacket.class, new ComponentPacketHandler());
        registerPacketHandlers(PositionPacket.class, new PositionPacketHandler());
        registerPacketHandlers(MovementPacket.class, new MovementPacketHandler());
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
