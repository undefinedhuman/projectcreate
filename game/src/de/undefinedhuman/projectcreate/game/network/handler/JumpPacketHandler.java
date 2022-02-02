package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class JumpPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof JumpPacket)) return false;
        JumpPacket movementPacket = (JumpPacket) packet;
        JumpPacket.parse(EntityManager.getInstance(), movementPacket);
        return true;
    }
}
