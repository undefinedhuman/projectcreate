package de.undefinedhuman.projectcreate.core.network.packets.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class ComponentPacket implements Packet {

    public long worldID;
    public String componentData;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static void serialize(Entity entity, Class<? extends Component>... componentClasses) {
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity does not contain id component, can not create component packet.");
            return;
        }
        ComponentPacket componentPacket = new ComponentPacket();
        componentPacket.worldID = idComponent.getWorldID();
        componentPacket.componentData = PacketUtils.createComponentData(entity.getComponents().);
    }

}
