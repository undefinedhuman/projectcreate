package de.undefinedhuman.projectcreate.game.inventory.utils;

import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;

public class InventoryUtils {

    public static InvItem getSelectedItemFromInventoryComponent(InventoryComponent inventoryComponent, String inventoryName) {
        Inventory selectorInventory;
        if((selectorInventory = inventoryComponent.getInventory(inventoryName)) == null || !(selectorInventory instanceof SelectorInventory))
            return null;
        return selectorInventory.getInvItem(0, ((SelectorInventory) selectorInventory).selectedIndex);
    }

}
