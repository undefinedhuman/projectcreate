package de.undefinedhuman.projectcreate.core.network.packets.entity;

import com.badlogic.ashley.core.Entity;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class CreateEntityPacket implements Packet {

    private long worldID = IDComponent.UNDEFINED;
    private int blueprintID = IDComponent.UNDEFINED;
    private int entityMasks = 0;
    private String componentData;

    private CreateEntityPacket() {}

    public static CreateEntityPacket serialize(Entity entity) {
        CreateEntityPacket packet = new CreateEntityPacket();
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity has no ID Component, might be a bug!");
            return packet;
        }
        packet.componentData = PacketUtils.createComponentData(entity.getComponents());
        packet.blueprintID = idComponent.getBlueprintID();
        packet.worldID = idComponent.getWorldID();
        packet.entityMasks = entity.flags;
        return packet;
    }

    public int getBlueprintID() {
        return blueprintID;
    }

    public long getWorldID() {
        return worldID;
    }

    public int getEntityMasks() {
        return entityMasks;
    }

    public String getComponentData() {
        return componentData;
    }
}
