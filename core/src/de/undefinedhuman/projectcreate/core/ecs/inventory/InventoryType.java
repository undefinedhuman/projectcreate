package de.undefinedhuman.projectcreate.core.ecs.inventory;

import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.core.inventory.SelectorInventory;

public enum InventoryType {
    INVENTORY(Inventory.class),
    SELECTOR(SelectorInventory.class),
    CRAFTING(Inventory.class);

    private final Class<? extends Inventory> inventoryClass;

    InventoryType(Class<? extends Inventory> inventoryClass) {
        this.inventoryClass = inventoryClass;
    }

    public Class<? extends Inventory> getInventoryClass() {
        return inventoryClass;
    }

}
