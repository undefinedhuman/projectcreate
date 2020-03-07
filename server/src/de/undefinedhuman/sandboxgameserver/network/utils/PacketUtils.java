package de.undefinedhuman.sandboxgameserver.network.utils;

import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.file.LineSplitter;
import de.undefinedhuman.sandboxgameserver.network.packets.entity.ComponentPacket;
import de.undefinedhuman.sandboxgameserver.network.packets.world.BlockPacket;
import de.undefinedhuman.sandboxgameserver.utils.Variables;
import de.undefinedhuman.sandboxgameserver.world.World;
import de.undefinedhuman.sandboxgameserver.world.WorldManager;

public class PacketUtils {

    public static void handleBlockPacket(BlockPacket packet) {

        World world = WorldManager.instance.getWorld(packet.worldName);

        if (packet.main) world.mainLayer.setBlock(packet.x, packet.y, (byte) (packet.id == -1 ? 0 : packet.id));
        else world.backLayer.setBlock(packet.x, packet.y, (byte) (packet.id == -1 ? 0 : packet.id));

    }

    public static void handleComponentPacket(ComponentPacket packet) {

        Entity entity = WorldManager.instance.getWorld(packet.worldName).getEntity(packet.worldID);
        if (entity != null) {

            LineSplitter s = new LineSplitter(packet.data, true, Variables.SEPARATOR);
            int size = s.getNextInt();
            for (int i = 0; i < size; i++) {
                String s1 = s.getNextString();
                ComponentType type = ComponentType.valueOf(s1);
                entity.getComponent(type).setNetworkData(s);
            }

        }

    }

}
