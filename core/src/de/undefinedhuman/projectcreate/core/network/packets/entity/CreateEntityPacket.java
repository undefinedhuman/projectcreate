package de.undefinedhuman.projectcreate.core.network.packets.entity;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;

public class CreateEntityPacket implements Packet {

    public long worldID = IDComponent.UNDEFINED;
    public int blueprintID = IDComponent.UNDEFINED;
    public String componentData;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static CreateEntityPacket serialize(Entity entity) {
        CreateEntityPacket entityPacket = new CreateEntityPacket();
        entityPacket.componentData = PacketUtils.createComponentData(entity.getComponents());
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity has no ID Component, might be a bug!");
            return entityPacket;
        }
        entityPacket.blueprintID = idComponent.getBlueprintID();
        entityPacket.worldID = idComponent.getWorldID();
        return entityPacket;
    }

    public static Entity parse(CreateEntityPacket entityPacket) {
        if(!BlueprintManager.getInstance().hasBlueprint(entityPacket.blueprintID) && RessourceUtils.existBlueprint(entityPacket.blueprintID)) {
            Log.debug("Error while loading entity blueprint. ID: " + entityPacket.blueprintID);
            return null;
        }
        Entity entity = BlueprintManager.getInstance().createEntity(entityPacket.blueprintID, entityPacket.worldID);
        PacketUtils.setComponentData(entity, PacketUtils.parseComponentData(entityPacket.componentData));
        return entity;
    }
}
