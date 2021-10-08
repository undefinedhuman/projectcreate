package de.undefinedhuman.projectcreate.core.network.packets.entity.components;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class PositionPacket implements Packet {

    public long timeStamp;
    public long worldID;
    public float x, y;
    public float velX, velY;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static PositionPacket serialize(Entity entity) {
        IDComponent idComponent = Mappers.ID.get(entity);
        if(idComponent == null) {
            Log.debug("Entity does not contain id component, cannot create component packet.");
            return null;
        }
        PositionPacket positionPacket = new PositionPacket();
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        positionPacket.worldID = idComponent.getWorldID();
        positionPacket.x = transformComponent.getX();
        positionPacket.y = transformComponent.getY();
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        positionPacket.velX = movementComponent.velocity.x;
        positionPacket.velY = movementComponent.velocity.y;
        positionPacket.timeStamp = System.currentTimeMillis();
        return positionPacket;
    }

}
