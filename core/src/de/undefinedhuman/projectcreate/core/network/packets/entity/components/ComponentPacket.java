package de.undefinedhuman.projectcreate.core.network.packets.entity.components;

import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentMapper;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ComponentPacket implements Packet {

    private long worldID;
    private String componentData;

    private ComponentPacket() {}

    @SafeVarargs
    public static ComponentPacket serialize(Entity entity, ComponentMapper<? extends Component>... components) {
        ComponentPacket componentPacket = new ComponentPacket();
        componentPacket.worldID = entity.getWorldID();
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
