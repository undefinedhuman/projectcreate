package de.undefinedhuman.projectcreate.core.network.utils;

import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.entity.Entity;
import de.undefinedhuman.projectcreate.core.entity.EntityManager;
import de.undefinedhuman.projectcreate.core.equip.EquipManager;
import de.undefinedhuman.projectcreate.core.network.packets.entity.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.EquipPacket;
import de.undefinedhuman.projectcreate.core.network.packets.world.BlockPacket;
import de.undefinedhuman.projectcreate.core.world.World;
import de.undefinedhuman.projectcreate.core.world.WorldManager;

public class PacketUtils {

    public static ComponentPacket createComponentPacket(Entity entity, ComponentType... types) {

        ComponentPacket packet = new ComponentPacket();
        packet.worldID = GameManager.instance.player.getWorldID();
        packet.worldName = World.instance.name;
        // packet.data = entity.send(types);
        return packet;

    }

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
        if (packet.blockID == -1) WorldManager.instance.destroyBlock(packet.x, packet.y, packet.worldLayer, false);
        else WorldManager.instance.placeBlock(packet.x, packet.y, packet.worldLayer, packet.blockID, false);
    }

    public static void handleComponentPacket(ComponentPacket packet) {

        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if (entity != null) {
            LineSplitter s = new LineSplitter(packet.data, true, Variables.SEPARATOR);
            int size = s.getNextInt();
            for (int i = 0; i < size; i++)
                entity.getComponent(ComponentType.valueOf(s.getNextString())).receive(s);
        }

    }

    public static void handleEquipComponent(EquipPacket packet) {

        Entity entity = EntityManager.getInstance().getEntity(packet.entityID);
        if (entity != null) {
            if (packet.equip) EquipManager.getInstance().equipItem(entity, packet.equipedItemID, packet.armor);
            else EquipManager.getInstance().unEquipItem(entity, packet.equipedItemID, packet.armor);
        }

    }

}
