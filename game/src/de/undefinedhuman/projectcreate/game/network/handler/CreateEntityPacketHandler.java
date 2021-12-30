package de.undefinedhuman.projectcreate.game.network.handler;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;

public class CreateEntityPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof CreateEntityPacket)) return false;
        CreateEntityPacket entityPacket = (CreateEntityPacket) packet;
        if(!BlueprintManager.getInstance().hasBlueprint(entityPacket.getBlueprintID()) && !RessourceUtils.existBlueprint(entityPacket.getBlueprintID())) {
            Log.error("Error while loading entity blueprint. ID: " + entityPacket.getBlueprintID());
            return false;
        }
        Entity entity = BlueprintManager.getInstance().createEntity(entityPacket.getBlueprintID(), entityPacket.getWorldID(), entityPacket.getEntityMasks());
        PacketUtils.setComponentData(entity, PacketUtils.parseComponentData(entityPacket.getComponentData()));
        EntityManager.getInstance().addEntity(entityPacket.getWorldID(), entity);
        return true;
    }
}
