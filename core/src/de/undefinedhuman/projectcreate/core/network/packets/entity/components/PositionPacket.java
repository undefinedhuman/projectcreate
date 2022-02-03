package de.undefinedhuman.projectcreate.core.network.packets.entity.components;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;

public class PositionPacket implements Packet {

    private long timeStamp;
    private long worldID;
    private float x, y;
    private float velX, velY;

    private PositionPacket() {}

    public static PositionPacket serialize(Entity entity) {
        PositionPacket positionPacket = new PositionPacket();
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        positionPacket.worldID = entity.getWorldID();
        positionPacket.x = transformComponent.getX();
        positionPacket.y = transformComponent.getY();
        positionPacket.velX = movementComponent.velocity.x;
        positionPacket.velY = movementComponent.velocity.y;
        positionPacket.timeStamp = System.currentTimeMillis();
        return positionPacket;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getWorldID() {
        return worldID;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2 getVelocity() {
        return new Vector2(velX, velY);
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }
}
