package de.undefinedhuman.projectcreate.core.ecs.inventory;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

import java.util.Collection;
import java.util.HashMap;

public class InventoryComponent implements Component, NetworkSerializable {

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

    @Override
    public void send(LineWriter writer) {
        inventories.forEach((name, inventory) -> {
            writer.writeString(name);
            InvItem[][] items = inventory.getInventory();
            writer.writeInt(inventory.getRow()).writeInt(inventory.getCol());
            for(int i = 0; i < inventory.getRow(); i++)
                for(int j = 0; j < inventory.getCol(); j++)
                    writer.writeInt(items[i][j].getID()).writeInt(items[i][j].getAmount());
        });
    }

    @Override
    public void receive(LineSplitter splitter) {
        while(splitter.hasMoreValues()) {
            Inventory inventory = inventories.get(splitter.getNextString());
            int row = splitter.getNextInt(), col = splitter.getNextInt();
            for(int i = 0; i < row; i++)
                for(int j = 0; j < col; j++)
                    inventory.getInvItem(i, j).setItem(splitter.getNextInt(), splitter.getNextInt());
        }
    }
}
