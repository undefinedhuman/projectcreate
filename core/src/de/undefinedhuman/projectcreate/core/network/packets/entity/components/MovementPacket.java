package de.undefinedhuman.projectcreate.core.network.packets.entity.components;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class MovementPacket implements Packet {

    public long worldID;
    public float delay;
    public int direction;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static MovementPacket serialize(Entity entity, MovementComponent component) {
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity does not contain id component, cannot create component packet.");
            return null;
        }
        MovementPacket packet = new MovementPacket();
        packet.worldID = idComponent.getWorldID();
        packet.direction = component.getDirection();
        return packet;
    }

    public static void parse(Entity entity, MovementPacket packet) {
        MovementComponent component = Mappers.MOVEMENT.get(entity);
        if(component == null) return;
        component.move(packet.direction);
    }

}
