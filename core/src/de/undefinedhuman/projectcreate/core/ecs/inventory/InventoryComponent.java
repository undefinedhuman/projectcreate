package de.undefinedhuman.projectcreate.core.ecs.inventory;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

import java.util.HashMap;

public class InventoryComponent implements Component, NetworkSerializable {

    private HashMap<String, Inventory> inventories;
    public InvItem currentlySelectedItem = new InvItem();

    public InventoryComponent(HashMap<String, InventoryData> inventoryData) {
        this.inventories = new HashMap<>();
        inventoryData.forEach((key, data) -> {
            Inventory inventory = data.createInventory();
            if(inventory == null) return;
            inventories.put(key, inventory);
        });
    }

    public void delete() {
        inventories.clear();
    }

    public boolean hasInventory(String name) {
        return inventories.containsKey(name);
    }

    public Inventory getInventory(String key) {
        return inventories.get(key);
    }

    public <T> T getInventory(InventoryType type, String key) {
        Inventory inventory = getInventory(key);
        if(!type.getInventoryClass().isInstance(inventory))
            return null;
        return (T) inventory;
    }

    // SAVING MAKE SURE THE SELECTED INVENTORY ITEM GETS DROPPED INTO WORLD WITH UNLIMITED DESPAWN DURATION

    @Override
    public void serialize(LineWriter writer) {
        inventories.forEach((name, inventory) -> {
            if(name == null || inventory == null) return;
            writer.writeString(name);
            inventory.serialize(writer);
        });
    }

    @Override
    public void parse(LineSplitter splitter) {
        while(splitter.hasMoreValues()) {
            Inventory inventory = inventories.get(splitter.getNextString());
            if(inventory == null) continue;
            inventory.parse(splitter);
        }
    }
}
