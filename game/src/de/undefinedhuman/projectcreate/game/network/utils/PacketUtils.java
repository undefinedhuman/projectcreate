package de.undefinedhuman.projectcreate.game.network.utils;

import com.badlogic.ashley.core.Entity;
import de.undefinedhuman.projectcreate.game.network.packets.entity.ComponentPacket;
import de.undefinedhuman.projectcreate.game.network.packets.inventory.EquipPacket;
import de.undefinedhuman.projectcreate.game.network.packets.world.BlockPacket;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.WorldManager;

public class PacketUtils {

    public static BlockPacket createBlockPacket(int x, int y, byte worldLayer, byte blockID) {

        BlockPacket packet = new BlockPacket();
        packet.worldName = World.instance.name;
        packet.blockID = blockID;
        packet.x = x;
        packet.y = y;
        packet.worldLayer = worldLayer;
        return packet;

    }

    public static EquipPacket createEquipPacket(Entity entity, int equipID, int equipedItemID, boolean equip, boolean armor) {

        EquipPacket packet = new EquipPacket();
        packet.equipID = equipID;
        packet.equip = equip;
        packet.equipedItemID = equipedItemID;
        packet.armor = armor;
        return packet;

    }

    public static void handleBlockPacket(BlockPacket packet) {
        if (packet.blockID == -1) WorldManager.getInstance().destroyBlock(packet.x, packet.y, packet.worldLayer, false);
        else WorldManager.getInstance().placeBlock(packet.x, packet.y, packet.worldLayer, packet.blockID, false);
    }

    public static void handleComponentPacket(ComponentPacket packet) {

    }

    public static void handleEquipComponent(EquipPacket packet) {

    }

}
