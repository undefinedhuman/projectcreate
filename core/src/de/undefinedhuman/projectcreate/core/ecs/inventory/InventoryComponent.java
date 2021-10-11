package de.undefinedhuman.projectcreate.core.ecs.inventory;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;

import java.util.Collection;
import java.util.HashMap;

public class InventoryComponent implements Component {

    private HashMap<String, Inventory> inventories;

    public InventoryComponent(HashMap<String, InventoryData> inventoryData) {
        this.inventories = new HashMap<>();
        inventoryData.forEach((key, data) -> inventories.put(key, data.createInventory()));
    }

    public void delete() {
        inventories.clear();
    }

    public Collection<Inventory> getInventories() {
        return inventories.values();
    }
    public Inventory getInventory(String key) {
        return inventories.get(key);
    }

}
