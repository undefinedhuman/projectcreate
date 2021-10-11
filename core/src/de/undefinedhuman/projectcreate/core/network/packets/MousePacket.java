package de.undefinedhuman.projectcreate.core.network.packets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.utils.Mouse;

public class MousePacket implements Packet {

    public long worldID;
    public float mouseX, mouseY;
    public boolean left, right;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static MousePacket serialize(Vector2 mouseInWorldPosition) {
        MousePacket packet = new MousePacket();
        packet.mouseX = mouseInWorldPosition.x;
        packet.mouseY = mouseInWorldPosition.y;
        packet.left = Mouse.isLeftClicked();
        packet.right = Mouse.isRightClicked();
        return packet;
    }

    public static void parse(Entity entity, MousePacket packet) {
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        if(mouseComponent == null) return;
        mouseComponent.mousePosition.set(packet.mouseX, packet.mouseY);
        mouseComponent.isLeftMouseButtonDown = packet.left;
        mouseComponent.isRightMouseButtonDown = packet.right;
    }

}
