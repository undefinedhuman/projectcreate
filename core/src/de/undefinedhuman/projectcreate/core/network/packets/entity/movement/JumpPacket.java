package de.undefinedhuman.projectcreate.core.network.packets.entity.movement;

import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class JumpPacket implements Packet {

    private long worldID;

    private JumpPacket() {}

    public static JumpPacket serialize(long worldID) {
        JumpPacket packet = new JumpPacket();
        packet.worldID = worldID;
        return packet;
    }

    public static void parse(EntityManager entityManager, JumpPacket packet) {
        Entity entity = entityManager.getEntity(packet.worldID);
        if(entity == null) return;
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        if(movementComponent == null) return;
        movementComponent.jump();
    }

}
