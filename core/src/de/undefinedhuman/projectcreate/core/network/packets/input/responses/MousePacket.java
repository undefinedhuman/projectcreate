package de.undefinedhuman.projectcreate.core.network.packets.input.responses;

import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class MousePacket implements Packet {
    private long worldID;
    private float mouseX, mouseY;
    private boolean left, right, canShake;

    private MousePacket() {}

    public static MousePacket serialize(long worldID, float mouseX, float mouseY, boolean left, boolean right, boolean canShake) {
        MousePacket packet = new MousePacket();
        packet.worldID = worldID;
        packet.mouseX = mouseX;
        packet.mouseY = mouseY;
        packet.left = left;
        packet.right = right;
        packet.canShake = canShake;
        return packet;
    }

    public static void parse(EntityManager entityManager, MousePacket packet) {
        Entity entity = entityManager.getEntity(packet.worldID);
        if(entity == null) {
            Log.warn("No entity with world id " + packet.worldID + " found!");
            return;
        }
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        if(mouseComponent == null) return;
        setMouseComponentData(mouseComponent, packet.mouseX, packet.mouseY, packet.left, packet.right, packet.canShake);
    }

    public static void setMouseComponentData(MouseComponent mouseComponent, float mouseX, float mouseY, boolean left, boolean right, boolean canShake) {
        mouseComponent.mousePosition.set(mouseX, mouseY);
        mouseComponent.isLeftMouseButtonDown = left;
        mouseComponent.isRightMouseButtonDown = right;
        mouseComponent.canShake = canShake;
    }

}
