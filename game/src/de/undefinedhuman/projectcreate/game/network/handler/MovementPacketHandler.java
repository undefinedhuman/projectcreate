package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;

public class MovementPacketHandler implements PacketHandler {

    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof MovementPacket)) return false;
        MovementPacket movementPacket = (MovementPacket) packet;
        MovementPacket.parse(EntityManager.getInstance(), movementPacket);
        return true;
    }

}
