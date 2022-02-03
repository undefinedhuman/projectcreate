package de.undefinedhuman.projectcreate.game.network;

import de.undefinedhuman.projectcreate.core.network.PacketListener;
import de.undefinedhuman.projectcreate.core.network.packets.auth.EncryptionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.packets.input.responses.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.input.responses.SelectorPacket;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.game.network.handler.ClientEncryptionPacketHandler;
import de.undefinedhuman.projectcreate.game.network.handler.CreateEntityPacketHandler;
import de.undefinedhuman.projectcreate.game.network.handler.PositionPacketHandler;

public class ClientListener extends PacketListener {

    private static volatile ClientListener instance;

    private ClientListener() {
        registerPacketHandlers(EncryptionPacket.class, new ClientEncryptionPacketHandler());
        registerPacketHandlers(CreateEntityPacket.class, new CreateEntityPacketHandler());
        registerPacketHandlers(RemoveEntityPacket.class, (connection, removeEntityPacket) -> RemoveEntityPacket.parse(EntityManager.getInstance(), removeEntityPacket));
        registerPacketHandlers(ComponentPacket.class, (connection, componentPacket) -> ComponentPacket.parse(EntityManager.getInstance(), componentPacket));
        registerPacketHandlers(PositionPacket.class, new PositionPacketHandler());
        registerPacketHandlers(MovementPacket.class, (connection, movementPacket) -> MovementPacket.parse(EntityManager.getInstance(), movementPacket));
        registerPacketHandlers(JumpPacket.class, (connection, jumpPacket) -> JumpPacket.parse(EntityManager.getInstance(), jumpPacket));
        registerPacketHandlers(MousePacket.class, (connection, mousePacket) -> MousePacket.parse(EntityManager.getInstance(), mousePacket));
        registerPacketHandlers(SelectorPacket.class, (connection, selectorPacket) -> SelectorPacket.parse(EntityManager.getInstance(), selectorPacket));
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
