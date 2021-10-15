package de.undefinedhuman.projectcreate.core.network.packets;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.utils.Mouse;

public class MousePacket implements Packet {

    public long worldID;
    public float mouseX, mouseY;
    public boolean left, right, canShake;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static void parse(Entity entity, MousePacket packet) {
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        if(mouseComponent == null) return;
        mouseComponent.mousePosition.set(packet.mouseX, packet.mouseY);
        mouseComponent.isLeftMouseButtonDown = packet.left;
        mouseComponent.isRightMouseButtonDown = packet.right;
        mouseComponent.canShake = packet.canShake;
    }

    public static MousePacket serialize(Entity entity) {
        MousePacket packet = new MousePacket();
        MouseComponent mouseComponent;
        if(entity == null || (mouseComponent = Mappers.MOUSE.get(entity)) == null)
            return packet;
        packet.mouseX = mouseComponent.mousePosition.x;
        packet.mouseY = mouseComponent.mousePosition.y;
        packet.canShake = mouseComponent.canShake;
        packet.left = Mouse.isLeftClicked();
        packet.right = Mouse.isRightClicked();
        return packet;
    }

}
