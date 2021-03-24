package de.undefinedhuman.projectcreate.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.items.ItemType;
import de.undefinedhuman.projectcreate.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.inventory.InvItem;
import de.undefinedhuman.projectcreate.item.ItemManager;

public class InvSlot extends Slot {

    protected InvItem invItem;
    private ItemType itemType;

    public InvSlot(Constraint x, Constraint y) {
        this(x, y, null);
    }

    public InvSlot(Constraint x, Constraint y, ItemType type) {
        super(x, y);
        this.itemType = type;
    }

    @Override
    public void init() {
        super.init();
        invItem = new InvItem(0, -1);
        invItem.parent = this;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        invItem.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        invItem.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        invItem.delete();
    }

    public boolean isTypeCompatible(InvItem item) {
        return itemType == null || ItemManager.instance.getItem(item.getID()).type == itemType;
    }

    public int addItem(InvItem item) {
        return this.addItem(item.getID(), item.getAmount());
    }

    public int addItem(int id, int amount) {
        if(id != 0 && amount != 0) {
            if(invItem.getAmount() != -1) {
                if (id != invItem.getID())
                    return amount;
                int maxAmount = ItemManager.instance.getItem(id).maxAmount.getInt();
                if ((invItem.getAmount() + amount) <= maxAmount)
                    setInvItem(invItem.getAmount() + amount);
                else {
                    int currentAmount = invItem.getAmount();
                    setInvItem(maxAmount);
                    return amount - (maxAmount - currentAmount);
                }
            } else setInvItem(id, amount);
        }
        return 0;
    }

    public void removeItem(int amount) {
        int tempAmount = this.invItem.getAmount() - amount;
        if(tempAmount <= 0) {
            deleteItem();
            return;
        }
        setInvItem(tempAmount);
    }

    public void deleteItem() {
        setInvItem(0, -1);
    }

    public InvItem getItem() {
        return this.invItem;
    }

    public ItemType getItemType() { return itemType; }

    public void setInvItem(int id, int amount) {
        this.invItem.setStats(id, amount);
    }

    protected void setInvItem(int amount) {
        this.setInvItem(invItem.getID(), amount);
    }

}
