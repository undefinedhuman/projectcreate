package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.items.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class Inventory extends Gui implements InvTarget {

    private InvSlot[][] inventory;
    private int row, col;

    private Vector2 offset = new Vector2();

    public Inventory(int row, int col) {
        this(row, col, null, GuiTemplate.SMALL_PANEL);
    }

    public Inventory(int row, int col, Vector2 offset, GuiTemplate template) {

        super(template);
        this.row = row;
        this.col = col;
        inventory = new InvSlot[row][col];
        this.offset.set(offset != null ? offset : new Vector2(template.cornerSize, template.cornerSize));
        setScale(Variables.getInventoryWidth(this.offset.x, col), Variables.getInventoryHeight(this.offset.y, row));

        for (int i = 0; i < inventory.length; i++)
            for (int j = 0; j < inventory[i].length; j++) {
                inventory[i][j] = new InvSlot();
                inventory[i][j].parent = this;
                inventory[i][j].setPosition(
                        "p" + (this.offset.x + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * j),
                        "p" + (this.offset.y + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i));
            }

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (InvSlot[] inv : inventory) for (InvSlot slot : inv) slot.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        super.render(batch, camera);
        if (visible)
            for (InvSlot[] invSlots : inventory) for (InvSlot invSlot : invSlots) invSlot.render(batch, camera);

    }

    @Override
    public Slot getClickedSlot(OrthographicCamera camera) {
        if (!visible) return null;
        for (InvSlot[] invSlots : inventory)
            for (InvSlot invSlot : invSlots) if (invSlot.isClicked(camera)) return invSlot;
        return null;
    }

    public boolean isFull(int id, int amount) {

        int localAmount = 0;

        for (int i = inventory.length - 1; i >= 0; i--)
            for (int j = 0; j < inventory[i].length; j++) {

                InvSlot slot = inventory[i][j];

                if (slot.getItem() != null) {
                    if (slot.getItem().getID() == id && ItemManager.instance.getItem(slot.getItem().getID()).isStackable)
                        localAmount += ItemManager.instance.getItem(slot.getItem().getID()).maxAmount - slot.getItem().getAmount();
                } else localAmount += ItemManager.instance.getItem(id).maxAmount;

            }

        return amount - localAmount > 0;

    }

    public InvSlot isFull(int id) {

        for (int i = inventory.length - 1; i >= 0; i--)
            for (int j = 0; j < inventory[i].length; j++) {

                InvSlot slot = inventory[i][j];

                if (slot.getItem() != null) {
                    if (slot.getItem().getID() == id && ItemManager.instance.getItem(slot.getItem().getID()).isStackable && slot.getItem().getAmount() < ItemManager.instance.getItem(slot.getItem().getID()).maxAmount)
                        return slot;
                } else return slot;

            }

        return null;

    }

    public boolean contains(int id) {

        boolean contains = false;
        for (InvSlot[] invSlots : inventory)
            for (InvSlot slot : invSlots)
                if (slot.getItem() != null) if (slot.getItem().getID() == id) if (!contains) contains = true;
        return contains;

    }

    public boolean contains(int id, int amount) {

        boolean contains = false;
        for (InvSlot[] invSlots : inventory)
            for (InvSlot slot : invSlots)
                if (slot.getItem() != null) if (slot.getItem().getID() == id && slot.getItem().getAmount() >= amount)
                    if (!contains) contains = true;
        return contains;

    }

    public InvSlot[][] getInv() {
        return this.inventory;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public int addItem(int id, int amount) {

        InvSlot slot = isFull(id);

        if (slot == null) return amount;
        else {
            int i = slot.addItem(id, amount);
            if (i == 0) return 0;
            return addItem(id, i);
        }

    }

    public void removeItem(int x, int y) {
        inventory[x][y].deleteItem();
    }

    public void removeItem(InvSlot slot) {
        slot.deleteItem();
    }

}
