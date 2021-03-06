package de.undefinedhuman.projectcreate.core.network.packets.entity.movement;

import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class MovementPacket implements Packet {

    private long worldID;
    private int direction;

    private MovementPacket() {}

    public static MovementPacket serialize(long worldID, int direction) {
        MovementPacket packet = new MovementPacket();
        packet.worldID = worldID;
        packet.direction = direction;
        return packet;
    }

    public static void parse(EntityManager entityManager, MovementPacket packet) {
        Entity entity = entityManager.getEntity(packet.worldID);
        if(entity == null) return;
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        if(movementComponent == null) return;
        movementComponent.move(packet.direction);
    }

    public long getWorldID() {
        return worldID;
    }

    public int getDirection() {
        return direction;
    }

}
