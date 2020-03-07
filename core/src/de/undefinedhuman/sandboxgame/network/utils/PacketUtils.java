package de.undefinedhuman.sandboxgame.network.utils;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.equip.EquipManager;
import de.undefinedhuman.sandboxgame.network.packets.entity.ComponentPacket;
import de.undefinedhuman.sandboxgame.network.packets.inventory.EquipPacket;
import de.undefinedhuman.sandboxgame.network.packets.world.BlockPacket;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Variables;
import de.undefinedhuman.sandboxgame.world.World;
import de.undefinedhuman.sandboxgame.world.WorldManager;

public class PacketUtils {

    public static ComponentPacket createComponentPacket(Entity entity, ComponentType... types) {

        ComponentPacket packet = new ComponentPacket();
        packet.worldID = GameManager.instance.player.getWorldID();
        packet.worldName = World.instance.name;
        packet.data = entity.send(types);
        return packet;

    }

    public static BlockPacket createBlockPacket(int id, float x, float y, boolean main) {

        BlockPacket packet = new BlockPacket();
        packet.worldName = World.instance.name;
        packet.id = id;
        packet.x = (int) x;
        packet.y = (int) y;
        packet.main = main;
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

        if (packet.id == -1) WorldManager.instance.destroyBlock(new Vector2(packet.x, packet.y), packet.main, false);
        else WorldManager.instance.placeBlock(packet.main, new Vector2(packet.x, packet.y), (byte) packet.id, false);

    }

    public static void handleComponentPacket(ComponentPacket packet) {

        Entity entity = EntityManager.instance.getEntity(packet.worldID);
        if (entity != null) {
            LineSplitter s = new LineSplitter(packet.data, true, Variables.SEPARATOR);
            int size = s.getNextInt();
            for (int i = 0; i < size; i++)
                entity.getComponent(ComponentType.valueOf(s.getNextString())).receive(s);
        }

    }

    public static void handleEquipComponent(EquipPacket packet) {

        Entity entity = EntityManager.instance.getEntity(packet.entityID);
        if (entity != null) {
            if (packet.equip) EquipManager.instance.equipItem(entity, packet.equipedItemID, packet.armor);
            else EquipManager.instance.unEquipItem(entity, packet.equipedItemID, packet.armor);
        }

    }

}
