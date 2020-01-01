package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.items.Item;
import de.undefinedhuman.sandboxgame.items.ItemManager;
import de.undefinedhuman.sandboxgame.items.ItemType;

public abstract class Slot extends Gui {

    private ItemType itemType;
    protected InvItem invItem = null;

    public Slot() {
        this(null);
    }

    public Slot(ItemType type) {
        super(GuiTemplate.SLOT);
        this.itemType = type;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(invItem != null) invItem.resize(width, height);
    }

    public void setItem(int id, int amount) {
        invItem = new InvItem(id, amount);
        invItem.parent = this;
        invItem.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setItem(InvItem item) {
        setItem(item.getID(), item.getAmount());
    }

    public int addItem(int id, int amount) {

        if(id != 0 && amount != 0) {

            Item type = ItemManager.instance.getItem(id);
            int maxAmount = type.maxAmount;
            boolean stackable = type.isStackable;

            if(invItem != null) {
                if(id == invItem.getID()) {
                    if(stackable && (invItem.getAmount() + amount) <= maxAmount) this.invItem.setAmount(invItem.getAmount() + amount);
                    else {
                        int currentAmount = invItem.getAmount();
                        this.invItem.setAmount(maxAmount);
                        return amount - (maxAmount - currentAmount);
                    }
                } else return amount;
            } else setItem(id, amount);

        }

        return 0;

    }

    public boolean isCompatible(InvItem item) { return itemType == null || ItemManager.instance.getItem(item.getID()).type == itemType; }
    public int addItem(InvItem item) {
        return this.addItem(item.getID(), item.getAmount());
    }
    public void removeItem(int amount) {
        this.invItem.setAmount(this.invItem.getAmount() - amount);
    }
    public void deleteItem() {
        this.invItem = null;
    }
    public InvItem getItem() {
        return this.invItem;
    }
    public ItemType getItemType() { return itemType; }

}
