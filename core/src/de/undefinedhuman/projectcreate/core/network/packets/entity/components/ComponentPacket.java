package de.undefinedhuman.projectcreate.core.network.packets.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ComponentPacket implements Packet {

    private long worldID;
    private String componentData;

    private ComponentPacket() {}

    @SafeVarargs
    public static ComponentPacket serialize(Entity entity, ComponentMapper<? extends Component>... components) {
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity does not contain id component, cannot create component packet.");
            return null;
        }
        ComponentPacket componentPacket = new ComponentPacket();
        componentPacket.worldID = idComponent.getWorldID();
        componentPacket.componentData = PacketUtils.createComponentData(Arrays.stream(components).map(componentMapper -> componentMapper.get(entity)).collect(Collectors.toList()));
        return componentPacket;
    }

    public static void parse(Entity entity, ComponentPacket packet) {
        PacketUtils.setComponentData(entity, PacketUtils.parseComponentData(packet.componentData));
    }

    public long getWorldID() {
        return worldID;
    }

}
