package de.undefinedhuman.projectcreate.core.network.packets.entity.movement;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class JumpPacket implements Packet {

    public long worldID = -1;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static JumpPacket serialize(Entity entity) {
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity does not contain id component, cannot create component packet.");
            return null;
        }
        return new JumpPacket();
    }

    public static void parse(Entity entity, JumpPacket packet) {
        MovementComponent component = Mappers.MOVEMENT.get(entity);
        if(component == null) return;
        component.jump();
    }
}
