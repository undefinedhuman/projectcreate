package de.undefinedhuman.projectcreate.game.network.handler;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class CreateEntityPacketHandler implements PacketHandler<CreateEntityPacket> {
    @Override
    public void handle(Connection connection, CreateEntityPacket packet) {
        Entity entity = EntityManager.getInstance().createEntity(packet.getBlueprintID(), packet.getWorldID(), packet.getEntityMasks());
        if(entity == null) return;
        PacketUtils.setComponentData(entity, PacketUtils.parseComponentData(packet.getComponentData()));
        EntityManager.getInstance().addEntity(entity);
    }
}
