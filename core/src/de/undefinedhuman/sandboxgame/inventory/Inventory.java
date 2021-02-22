package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class Inventory extends Gui implements InvTarget {

    protected InvSlot[][] inventory;
    private int row, col;

    public Inventory(int row, int col) {
        this(row, col, GuiTemplate.SMALL_PANEL);
    }

    public Inventory(int row, int col, GuiTemplate template) {
        super(template);
        inventory = new InvSlot[this.row = row][this.col = col];
        setSize(Tools.getInventoryConstraint(template, col), Tools.getInventoryConstraint(template, row));

        for (int i = 0; i < inventory.length; i++)
            for (int j = 0; j < inventory[i].length; j++) {
                addChild(
                        inventory[i][j] = (InvSlot) new InvSlot()
                                .setPosition(
                                        new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * j),
                                        new PixelConstraint((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i)
                                )
                );
            }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    @Override
    public Slot getClickedSlot(OrthographicCamera camera) {
        if (!visible) return null;
        for (InvSlot[] invSlots : inventory)
            for (InvSlot invSlot : invSlots)
                if (invSlot.isClicked(camera)) return invSlot;
        return null;
    }

    public int addItem(int id, int amount) {
        InvSlot slot = isFull(id);
        if (slot == null) return amount;
        else {
            int newAmount = slot.addItem(id, amount);
            return newAmount == 0 ? 0 : addItem(id, newAmount);
        }
    }

    public int removeItem(int id, int amountToBeRemoved) {
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem currentItem = inventory[i][j].getItem();
                if(currentItem == null || currentItem.getID() != id)
                    continue;
                int amountOfCurrentSlot = currentItem.getAmount();
                inventory[i][j].removeItem(amountToBeRemoved);
                amountToBeRemoved = Math.max(amountToBeRemoved - amountOfCurrentSlot, 0);
            }

        return amountToBeRemoved;
    }

    public boolean isFull(int id, int amount) {
        int currentAmount = 0, maxAmount = ItemManager.instance.getItem(id).maxAmount.getInt();

        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvItem currentItem = inventory[i][j].getItem();
                if(currentItem == null) {
                    currentAmount += maxAmount;
                    continue;
                }
                if(currentItem.getID() != id || currentItem.getAmount() == maxAmount) continue;
                currentAmount += maxAmount - currentItem.getAmount();
            }

        return amount - currentAmount > 0;
    }

    public InvSlot isFull(int id) {
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvSlot slot = inventory[i][j];
                InvItem currentItem = slot.getItem();
                if(currentItem == null || (currentItem.getID() == id && currentItem.getAmount() < ItemManager.instance.getItem(id).maxAmount.getInt())) return slot;
            }
        return null;
    }

    public int amountOf(int id) {
        int total = 0;
        for (int i = row - 1; i >= 0; i--)
            for (int j = 0; j < col; j++) {
                InvSlot slot = inventory[i][j];
                InvItem currentItem = slot.getItem();
                if(currentItem != null && currentItem.getID() == id)
                    total += currentItem.getAmount();
            }
        return total;
    }

    public boolean contains(int id) {
        return this.contains(id, 1);
    }

    public boolean contains(int id, int amount) {
        int containsAmount = amount;

        for (InvSlot[] invSlots : inventory)
            for (InvSlot slot : invSlots) {
                InvItem currentItem = slot.getItem();
                if(currentItem == null || currentItem.getID() != id) continue;
                containsAmount -= currentItem.getAmount();
            }

        return containsAmount <= 0;
    }

    public InvSlot[][] getInventory() {
        return inventory;
    }

}
