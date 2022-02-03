package de.undefinedhuman.projectcreate.core.network.packets.input.responses;

import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryType;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class SelectorPacket implements Packet {

    private long worldID;
    private String inventoryName;
    private int selectedIndex;

    private SelectorPacket() {}

    public static SelectorPacket serialize(long worldID, String inventoryName, int selectedIndex) {
        SelectorPacket packet = new SelectorPacket();
        packet.worldID = worldID;
        packet.inventoryName = inventoryName;
        packet.selectedIndex = selectedIndex;
        return packet;
    }

    public static void parse(EntityManager entityManager, SelectorPacket packet) {
        Entity entity = entityManager.getEntity(packet.worldID);
        if(entity == null) return;
        SelectorInventory selectorInventory = InventoryComponent.getInventoryOfEntity(entity, InventoryType.SELECTOR, packet.inventoryName);
        if(selectorInventory == null) return;
        selectorInventory.selectedIndex = Utils.clamp(packet.selectedIndex, 0, selectorInventory.getCol());
    }

}
