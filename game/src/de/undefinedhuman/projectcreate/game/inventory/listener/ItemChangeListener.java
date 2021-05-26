package de.undefinedhuman.projectcreate.game.inventory.listener;

@FunctionalInterface
public interface ItemChangeListener {
    /*public ItemChangeListener(int id, InventoryManager inventoryManager) {
        inventoryManager.addListener(id, this);
    }*/

    void notify(int amount);
}
