package de.undefinedhuman.projectcreate.core.network.packets.entity;

import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;

public class RemoveEntityPacket implements Packet {

    private long worldID;

    private RemoveEntityPacket() {}

    public static RemoveEntityPacket serialize(long worldID) {
        RemoveEntityPacket packet = new RemoveEntityPacket();
        packet.worldID = worldID;
        return packet;
    }

    public static void parse(EntityManager entityManager, RemoveEntityPacket packet) {
        entityManager.removeEntity(packet.worldID);
    }

}
