package de.undefinedhuman.projectcreate.core.network.packets.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

import java.util.HashMap;

public class CreateEntityPacket implements Packet {

    public long worldID;
    public int blueprintID;
    public String componentData;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static CreateEntityPacket create(int blueprintID, long worldID, Entity entity) {
        CreateEntityPacket entityPacket = new CreateEntityPacket();
        entityPacket.blueprintID = blueprintID;
        entityPacket.worldID = worldID;
        entityPacket.componentData = PacketUtils.createComponentData(entity.getComponents());
        return entityPacket;
    }

    public static Entity parse(CreateEntityPacket entityPacket) {
        if(!BlueprintManager.getInstance().hasBlueprint(entityPacket.blueprintID))
            return null;
        HashMap<String, LineSplitter> componentData = PacketUtils.parseComponentData(entityPacket.componentData);
        Entity entity = BlueprintManager.getInstance().getBlueprint(entityPacket.blueprintID).createInstance();
        for(Component component : entity.getComponents()) {
            if(!(component instanceof NetworkSerializable))
                continue;
            LineSplitter splitter = componentData.get(component.getClass().getSimpleName());
            if(splitter == null) continue;
            ((NetworkSerializable) component).receive(splitter);
        }
        return entity;
    }
}
