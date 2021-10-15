package de.undefinedhuman.projectcreate.core.network.packets;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class SelectorPacket implements Packet {

    public long worldID;
    public String inventoryName;
    public int selectedIndex;

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static SelectorPacket serialize(String inventoryName, int selectedIndex) {
        SelectorPacket packet = new SelectorPacket();
        packet.inventoryName = inventoryName;
        packet.selectedIndex = selectedIndex;
        return packet;
    }

    public static void parse(Entity entity, SelectorPacket packet) {
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(entity);
        if(inventoryComponent == null) return;
        Inventory inventory = inventoryComponent.getInventory(packet.inventoryName);
        if(!(inventory instanceof SelectorInventory)) return;
        ((SelectorInventory) inventory).selectedIndex = Utils.clamp(packet.selectedIndex, 0, inventory.getCol());
    }

}
