package de.undefinedhuman.projectcreate.game.equip;

import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;

public abstract class EquipSlot extends InvSlot {

    private Gui previewGui;

    public EquipSlot(Constraint x, Constraint y, ItemType equipType, String previewTexture) {
        super();
        previewGui = (Gui) new Gui(previewTexture)
                .setAlpha(0.5f)
                .set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE))
                .setOffset(new CenterOffset(), new CenterOffset());
        previewGui.parent = this;
    }

    @Override
    public void init() {
        super.init();
        previewGui.init();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        previewGui.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        previewGui.update(delta);
    }

    /*@Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if(invItem.getAmount() == -1)
            previewGui.render(batch, camera);
    }*/

    @Override
    public void delete() {
        super.delete();
        previewGui.delete();
    }

    /*@Override
    public void setInvItem(int id, int amount) {
        super.setInvItem(id, amount);
        equip();
    }

    @Override
    public void deleteItem() {
        unEquip();
        super.deleteItem();
    }*/

    public abstract void equip();

    public abstract void unEquip();

}
