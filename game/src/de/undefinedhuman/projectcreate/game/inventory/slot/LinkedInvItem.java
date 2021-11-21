package de.undefinedhuman.projectcreate.game.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.inventory.ItemChangeListener;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.gui.Gui;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class LinkedInvItem extends Gui {

    private int localID = 0, localAmount = 0;
    private Text amountText;
    private InvItem linkedItem;
    private ItemChangeListener itemChangeListener;

    public LinkedInvItem() {
        super(Variables.DEFAULT_TEXTURE);
        set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE));
        setOffset(new CenterOffset(), new CenterOffset());
        addChild(
                amountText = (Text) new Text(0)
                        .setFontSize(8)
                        .setPosition(new RelativeConstraint(1.2f), new RelativeConstraint(0)).setOffsetX(new RelativeOffset(-1f))
        );
        this.itemChangeListener = (id, amount) -> {
            if(localID != id) updateID(id);
            if(localAmount != amount) updateAmount(amount);
        };
    }

    @Override
    public void init() {
        super.init();
        update();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(isEmpty())
            return;
        super.render(batch, camera);
    }

    public void link(InvItem linkedItem) {
        this.linkedItem = linkedItem;
        this.linkedItem.addItemChangeListener(itemChangeListener);
        update();
    }

    public void unlink() {
        if(linkedItem != null)
            linkedItem.removeItemChangeListener(itemChangeListener);
        this.linkedItem = null;
    }

    private void update() {
        updateID(getID());
        updateAmount(getAmount());
    }

    private void updateID(int id) {
        this.localID = id;
        setTexture(ItemManager.getInstance().getItem(localID).iconTexture.getValue());
        resize();
    }

    private void updateAmount(int amount) {
        this.localAmount = amount;
        amountText.setText(localAmount).setVisible(this.localAmount > 1);
    }

    public boolean isLinked() {
        return linkedItem != null;
    }

    public boolean isTypeCompatible(int id) {
        return linkedItem != null && linkedItem.isTypeCompatible(id);
    }

    public boolean isEmpty() {
        return linkedItem == null || linkedItem.isEmpty();
    }

    public int getAmount() {
        return linkedItem == null ? -1 : linkedItem.getAmount();
    }

    public int getID() {
        return linkedItem == null ? 0 : linkedItem.getID();
    }

}
