package de.undefinedhuman.projectcreate.core.network.packets.entity.movement;

import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;

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
        if(entity == null || !Mappers.MOVEMENT.has(entity)) {
            Log.warn("Entity with world id " + packet.worldID + " does not exists!");
            return;
        }
        Mappers.MOVEMENT.get(entity).jump();
    }

}
