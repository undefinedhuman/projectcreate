package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.game.equip.EquipManager;
import de.undefinedhuman.projectcreate.game.inventory.InvItem;
import de.undefinedhuman.projectcreate.game.inventory.Inventory;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class Selector extends Inventory {

    private static volatile Selector instance;

    private int selected = 0;

    private Selector() {
        super(1, 9, GuiTemplate.HOTBAR);
        setPosition(new CenterConstraint(), new RelativeConstraint(1)).setOffset(new CenterOffset(), new PixelOffset(-Tools.getInventorySize(GuiTemplate.HOTBAR, 1) - 10));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (inventory[0][selected].getInvItem().getAmount() == -1)
            EquipManager.getInstance().unEquipItemNetwork(GameManager.getInstance().player, 0, false);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
    }

    public short getSelectedItemID() {
        InvItem item = inventory[0][selected].getInvItem();
        return (short) (item.getAmount() != -1 ? item.getID() : 0);
    }

    public void setSelected(int index) {
        if(InventoryManager.getInstance().isInventoryOpened())
            return;
        inventory[0][selected].setSelected(false);
        this.selected = index;
        InvItem item = inventory[0][selected].getInvItem();
        if (!item.isEmpty())
            EquipManager.getInstance().equipItemNetwork(GameManager.getInstance().player, item.getID(), false);
        else EquipManager.getInstance().unEquipItemNetwork(GameManager.getInstance().player, 0, false);
        inventory[0][selected].setSelected(true);
    }

    public Item getSelectedItem() {
        return ItemManager.getInstance().getItem(inventory[0][selected].getInvItem().getID());
    }

    public InvItem getSelectedInvItem() {
        return inventory[0][selected].getInvItem();
    }

    public int getSelected() {
        return this.selected;
    }

    public static Selector getInstance() {
        if(instance != null)
            return instance;
        synchronized (Selector.class) {
            if (instance == null)
                instance = new Selector();
        }
        return instance;
    }

}
