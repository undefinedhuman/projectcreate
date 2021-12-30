package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;

public class RemoveEntityPacketHandler implements PacketHandler {

    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof RemoveEntityPacket)) return false;
        RemoveEntityPacket removeEntityPacket = (RemoveEntityPacket) packet;
        EntityManager.getInstance().removeEntity(removeEntityPacket.getWorldID());
        return true;
    }
}
