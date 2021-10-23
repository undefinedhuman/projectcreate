package de.undefinedhuman.projectcreate.game.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.ItemChangeListener;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.gui.transforms.GuiTransform;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.ds.Poolable;

public class InvSlot extends Slot implements Poolable {

    private Gui icon;
    private int localID = 0, localAmount = 0;
    private Text amountText;
    private InvItem linkedItem;
    private ItemChangeListener itemChangeListener;
    private long linkedEntityID;
    private int row, col;
    private String linkInventoryName;

    public InvSlot() {
        super(new PixelConstraint(0), new PixelConstraint(0));
        icon = new Gui(Variables.DEFAULT_TEXTURE);
        icon.set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE));
        icon.setOffset(new CenterOffset(), new CenterOffset());
        icon.addChild(
                amountText = (Text) new Text(0)
                        .setFontSize(8)
                        .setPosition(new RelativeConstraint(1.2f), new RelativeConstraint(0)).setOffsetX(new RelativeOffset(-1f))
        );
        icon.parent = this;
        this.itemChangeListener = (id, amount) -> {
            if(localID != id) updateID(id);
            if(localAmount != amount) updateAmount(amount);
        };
        setVisible(false);
    }

    @Override
    public void init() {
        super.init();
        updateSlot();
    }

    @Override
    public void freeUp() {
        unlink();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        icon.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if(linkedItem.isEmpty())
            return;
        icon.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        if(this.icon != null)
            icon.delete();
    }

    @Override
    public GuiTransform setVisible(boolean visible) {
        this.icon.setVisible(visible);
        return super.setVisible(visible);
    }

    public void link(long entityID, String inventoryName, int row, int col, InvItem linkedItem) {
        setVisible(true);
        this.linkInventoryName = inventoryName;
        this.linkedEntityID = entityID;
        this.row = row;
        this.col = col;
        this.linkedItem = linkedItem;
        this.linkedItem.addItemChangeListener(itemChangeListener);
        setValue(Axis.X, (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * col);
        setValue(Axis.Y, (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * row);
        updateSlot(linkedItem.getID(), linkedItem.getAmount());
    }

    public void unlink() {
        this.parent = GuiManager.getInstance().screen;
        if(linkedItem != null)
            linkedItem.removeItemChangeListener(itemChangeListener);
        this.linkedItem = null;
        setVisible(false);
    }

    private void updateSlot() {
        this.updateSlot(localID, localAmount);
    }

    private void updateSlot(int id, int amount) {
        updateID(id);
        updateAmount(amount);
    }

    private void updateID(int id) {
        this.localID = id;
        this.icon.setTexture(ItemManager.getInstance().getItem(localID).iconTexture.getValue());
        this.icon.resize();
    }

    private void updateAmount(int amount) {
        this.localAmount = amount;
        amountText.setText(localAmount).setVisible(this.localAmount > 1);
    }

    public int addItem(int id, int amount) {
        if(linkedItem == null)
            return amount;
        return linkedItem.addItem(id, amount);
    }

    public void deleteItem() {
        if(linkedItem != null)
        linkedItem.deleteItem();
    }

    public boolean isTypeCompatible(int id) {
        return linkedItem != null && linkedItem.isTypeCompatible(id);
    }

    public boolean isEmpty() {
        return linkedItem == null || linkedItem.isEmpty();
    }

    public int getAmount() {
        return localAmount;
    }

    public int getID() {
        return localID;
    }

    public int removeItem(int amount) {
        if(linkedItem == null)
            return amount;
        return linkedItem.removeItem(amount);
    }

    public int getRow() {
        if(linkedItem == null) return -1;
        return row;
    }

    public int getCol() {
        if(linkedItem == null) return -1;
        return col;
    }

    public long getLinkedEntityID() {
        if(linkedItem == null) return -1;
        return linkedEntityID;
    }

    public String getLinkInventoryName() {
        if(linkedItem == null) return "";
        return linkInventoryName;
    }
}
