package de.undefinedhuman.projectcreate.core.inventory;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import java.util.ArrayList;

public class InvItem {

    private int id, amount;
    private Class<? extends Item> itemTypeFilter;

    private ArrayList<ItemChangeListener> listeners = new ArrayList<>();

    public InvItem() {
        this(0, 0, Item.class);
    }

    public InvItem(int id, int amount, Class<? extends Item> itemTypeFilter) {
        updateItem(id, amount);
        this.itemTypeFilter = itemTypeFilter;
    }

    public boolean isTypeCompatible(InvItem item) {
        return isTypeCompatible(item.id);
    }

    public boolean isTypeCompatible(int id) {
        return RessourceUtils.existItem(id) && (itemTypeFilter == Item.class || itemTypeFilter.isInstance(ItemManager.getInstance().getItem(id)));
    }

    public int addItem(InvItem item) {
        return this.addItem(item.getID(), item.getAmount());
    }

    public int addItem(int otherID, int otherAmount) {
        if(otherID <= 0 || otherAmount <= 0)
            return 0;
        int maxAmount = ItemManager.getInstance().getItem(otherID).maxAmount.getValue();
        if(!isEmpty()) {
            if (otherID != this.id)
                return otherAmount;
            if ((amount + otherAmount) <= maxAmount)
                updateItem(id, amount + otherAmount);
            else {
                int currentAmount = amount;
                updateItem(id, maxAmount);
                return otherAmount - (maxAmount - currentAmount);
            }
        } else {
            updateItem(otherID, Math.min(otherAmount, maxAmount));
            return Math.max(otherAmount - maxAmount, 0);
        }
        return 0;
    }

    public int removeItem(int otherAmount) {
        int tempAmount = amount - otherAmount;
        if(tempAmount <= 0) {
            deleteItem();
            return Math.abs(tempAmount);
        }
        updateItem(id, tempAmount);
        return 0;
    }

    public void deleteItem() {
        updateItem(0, -1);
    }

    public void setItem(Vector2i stats) {
        this.setItem(stats.x, stats.y);
    }

    public void setItem(int id, int amount) {
        updateItem(id, amount);
    }

    public int getID() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isEmpty() {
        return amount <= 0;
    }

    public void addItemChangeListener(ItemChangeListener listener) {
        if(listener == null || listeners.contains(listener))
            return;
        listeners.add(listener);
    }

    public void removeItemChangeListener(ItemChangeListener listener) {
        if(listener == null) return;
        listeners.remove(listener);
    }

    private void updateItem(int id, int amount) {
        if(id != 0 && !isTypeCompatible(id))
            return;
        this.id = id;
        this.amount = Utils.clamp(amount, 0, ItemManager.getInstance().getItem(id).maxAmount.getValue());
        listeners.forEach(listener -> listener.changed(this.id, this.amount));
    }
}
