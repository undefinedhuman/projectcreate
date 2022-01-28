package de.undefinedhuman.projectcreate.game.network.handler;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class ComponentPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof ComponentPacket)) return false;
        ComponentPacket componentPacket = (ComponentPacket) packet;
        Entity entity = EntityManager.getInstance().getEntity(componentPacket.getWorldID());
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return true;
        ComponentPacket.parse(entity, componentPacket);
        return true;
    }
}
