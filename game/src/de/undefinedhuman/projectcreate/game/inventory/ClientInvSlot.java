package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.ItemChangeListener;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.ds.Poolable;
import de.undefinedhuman.projectcreate.game.inventory.slot.Slot;

public class ClientInvSlot extends Slot implements Poolable {

    private Gui icon;
    private int localID = 0, localAmount = 0;
    private Text amountText;
    private InvItem linkedItem;
    private ItemChangeListener itemChangeListener;

    public ClientInvSlot() {
        super(new PixelConstraint(0), new PixelConstraint(0));
        icon = new Gui(Variables.DEFAULT_TEXTURE);
        icon.set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE));
        icon.setOffset(new CenterOffset(), new CenterOffset());
        icon.addChild(
                amountText = (Text) new Text(linkedItem.getAmount())
                        .setFontSize(8)
                        .setPosition(new RelativeConstraint(1.2f), new RelativeConstraint(0)).setOffsetX(new RelativeOffset(-1f))
        );
        icon.parent = this;
        this.itemChangeListener = (id, amount) -> {
            if(localID == id && localAmount == amount)
                return;
            updateIcon();
            updateAmountText();
        };
    }

    @Override
    public void init() {
        super.init();
        updateIcon();
        updateAmountText();
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
        if(linkedItem != null) this.linkedItem.removeItemChangeListener(itemChangeListener);
    }

    public void setLinkedItem(int row, int col, InvItem linkedItem) {
        this.linkedItem = linkedItem;
        this.linkedItem.addItemChangeListener(itemChangeListener);
        this.getConstraint(Axis.X).setValue((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * col);
        this.getConstraint(Axis.Y).setValue((Variables.SLOT_SIZE + Variables.SLOT_SPACE) * row);
    }

    private void updateIcon() {
        this.icon.setTexture(ItemManager.getInstance().getItem(localID).iconTexture.getValue());
        this.icon.resize();
    }

    private void updateAmountText() {
        amountText.setText(localAmount).setVisible(this.localAmount > 1);
    }

}
