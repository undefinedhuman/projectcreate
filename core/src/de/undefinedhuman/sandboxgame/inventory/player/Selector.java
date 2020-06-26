package de.undefinedhuman.sandboxgame.inventory.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.equip.EquipManager;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.inventory.InvItem;
import de.undefinedhuman.sandboxgame.inventory.Inventory;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;

public class Selector extends Inventory {

    private int selected = 0;

    public Selector() {
        super(1, 9, new Vector2(8, 8), GuiTemplate.HOTBAR);
        setPosition(new CenterConstraint(), new RelativeConstraint(1)).setOffset(new RelativeOffset(-0.5f), new PixelOffset(Variables.BASE_WINDOW_HEIGHT - 10));
        setVisible(false);
    }

    public void updateSelector() {
        ItemManager.instance.useItem(getSelectedItemID());
        if (getInv()[0][selected].getItem() == null)
            EquipManager.instance.unEquipItemNetwork(GameManager.instance.player, 0, false);
    }

    public int getSelectedItemID() {
        InvItem item = getInv()[0][selected].getItem();
        return item != null ? item.getID() : 0;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        refresh();
        super.render(batch, camera);
    }

    private void refresh() {
        for (int i = 0; i < getInv()[0].length; i++) {
            getInv()[0][i].setSelected(false);
        }
        if (!InventoryManager.instance.isInventoryOpened()) {
            getInv()[0][selected].setSelected(true);
        }
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int index) {
        if (!InventoryManager.instance.isInventoryOpened()) this.selected = index;
        InvItem item = getInv()[0][selected].getItem();
        if (item != null)
            EquipManager.instance.equipItemNetwork(GameManager.instance.player, getInv()[0][selected].getItem().getID(), false);
        else EquipManager.instance.unEquipItemNetwork(GameManager.instance.player, 0, false);
    }

    public Item getSelectedItem() {
        return ItemManager.instance.getItem(getInv()[0][selected].getItem().getID());
    }

    public InvItem getSelectedInvItem() {
        return getInv()[0][selected].getItem();
    }

}
