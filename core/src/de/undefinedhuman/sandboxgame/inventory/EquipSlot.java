package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;

public abstract class EquipSlot extends Slot {

    private Gui previewGui;

    public EquipSlot(String previewTexture, ItemType equipType) {
        super(equipType);
        setSize(new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
        previewGui = new Gui(previewTexture);
        previewGui.set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE));
        previewGui.setAlpha(0.5f);
        previewGui.parent = this;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        previewGui.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (invItem != null) {
            if (invItem.getAmount() > 0) invItem.render(batch, camera);
            else this.deleteItem();
        } else previewGui.render(batch, camera);
    }

    @Override
    public void setItem(int id, int amount) {
        super.setItem(id, amount);
        equip();
    }

    @Override
    public void deleteItem() {
        unequip();
        super.deleteItem();
    }

    public abstract void equip();

    public abstract void unequip();

}
