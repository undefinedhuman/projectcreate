package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.items.Item;
import de.undefinedhuman.projectcreate.game.equip.EquipManager;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.game.inventory.InvItem;
import de.undefinedhuman.projectcreate.game.inventory.Inventory;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.item.ItemManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class Selector extends Inventory {

    public static Selector instance;

    private int selected = 0;

    public Selector() {
        super(1, 9, GuiTemplate.HOTBAR);
        if(instance == null) instance = this;
        setPosition(new CenterConstraint(), new RelativeConstraint(1)).setOffset(new CenterOffset(), new PixelOffset(-Tools.getInventorySize(GuiTemplate.HOTBAR, 1) - 10));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        ItemManager.getInstance().useItem(getSelectedItemID());
        if (inventory[0][selected].getItem().getAmount() == -1)
            EquipManager.getInstance().unEquipItemNetwork(GameManager.instance.player, 0, false);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    public int getSelectedItemID() {
        InvItem item = inventory[0][selected].getItem();
        return item.getAmount() != -1 ? item.getID() : 0;
    }

    public void setSelected(int index) {
        if(InventoryManager.instance.isInventoryOpened())
            return;
        inventory[0][selected].setSelected(false);
        this.selected = index;
        InvItem item = inventory[0][selected].getItem();
        if (item.getAmount() != -1)
            EquipManager.getInstance().equipItemNetwork(GameManager.instance.player, item.getID(), false);
        else EquipManager.getInstance().unEquipItemNetwork(GameManager.instance.player, 0, false);
        inventory[0][selected].setSelected(true);
    }

    public Item getSelectedItem() {
        return ItemManager.getInstance().getItem(inventory[0][selected].getItem().getID());
    }

    public InvItem getSelectedInvItem() {
        return inventory[0][selected].getItem();
    }

    public int getSelected() {
        return this.selected;
    }

}
