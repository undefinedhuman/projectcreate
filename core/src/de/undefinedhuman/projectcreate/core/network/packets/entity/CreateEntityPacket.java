package de.undefinedhuman.projectcreate.core.network.packets.entity;

import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;

public class CreateEntityPacket implements Packet {

    private long worldID = Entity.UNDEFINED_WORLD_ID;
    private int blueprintID = Entity.UNDEFINED_BLUEPRINT_ID;
    private int entityMasks = 0;
    private String componentData;

    private CreateEntityPacket() {}

    public static CreateEntityPacket serialize(Entity entity) {
        CreateEntityPacket packet = new CreateEntityPacket();
        packet.blueprintID = entity.getBlueprintID();
        packet.worldID = entity.getWorldID();
        packet.entityMasks = entity.flags;
        packet.componentData = PacketUtils.createComponentData(entity.getComponents());
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
