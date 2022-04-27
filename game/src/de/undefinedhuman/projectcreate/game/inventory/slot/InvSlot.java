package de.undefinedhuman.projectcreate.game.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.SlotInfo;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.ds.Poolable;

public class InvSlot extends Slot implements Poolable {

    private LinkedInvItem invItem;
    private SlotInfo slotInfo = new SlotInfo();

    public InvSlot() {
        super(new PixelConstraint(0), new PixelConstraint(0));
        this.invItem = new LinkedInvItem();
        this.invItem.parent = this;
        setVisible(false);
    }

    @Override
    public void init() {
        super.init();
        invItem.init();
    }

    @Override
    public void freeUp() {
        unlink();
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
        if(this.invItem != null) invItem.delete();
    }

    @Override
    public GuiTransform setVisible(boolean visible) {
        this.invItem.setVisible(visible);
        return super.setVisible(visible);
    }

    public void link(long entityID, String inventoryName, int row, int col, InvItem linkedItem) {
        setVisible(true);
        invItem.link(linkedItem);
        slotInfo.link(entityID, row, col, inventoryName);
        setPosition((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * col, (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * row);
    }

    public void unlink() {
        this.parent = GuiManager.getInstance().screen;
        slotInfo.unlink();
        invItem.unlink();
        setVisible(false);
    }

    public SlotInfo getSlotInfo() {
        return slotInfo;
    }

    public boolean isTypeCompatible(int id) {
        return this.invItem.isTypeCompatible(id);
    }

    public boolean isEmpty() {
        return this.invItem.isEmpty();
    }

    public int getID() {
        return this.invItem.getID();
    }

    public int getAmount() {
        return this.invItem.getAmount();
    }

}
