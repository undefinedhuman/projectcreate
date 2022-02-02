package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.input.responses.MousePacket;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class MousePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof MousePacket)) return false;
        MousePacket mousePacket = (MousePacket) packet;
        MousePacket.parse(EntityManager.getInstance(), mousePacket);
        return true;
    }
}
