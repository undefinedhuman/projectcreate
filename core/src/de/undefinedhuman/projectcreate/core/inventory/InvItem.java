package de.undefinedhuman.projectcreate.core.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.gui.Gui;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.core.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.core.item.ItemManager;
import de.undefinedhuman.projectcreate.core.gui.text.Text;

public class InvItem extends Gui {

    private int id, amount;
    private Text amountText;

    public InvItem(int id) {
        this(id, -1);
    }

    public InvItem(int id, int amount) {
        super(ItemManager.instance.getItem(id).iconTexture.getString());
        updateItem(id, amount);
        set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE));
        setOffset(new CenterOffset(), new CenterOffset());
        addChild(
                amountText = (Text) new Text(amount)
                        .setFontSize(8)
                        .setPosition(new RelativeConstraint(1.2f), new RelativeConstraint(0)).setOffsetX(new RelativeOffset(-1f))
        );
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if(amount == -1)
            return;
        super.render(batch, camera);
    }

    public void setStats(int id, int amount) {
        updateItem(id, amount);
        updateAmountText();
        setTexture(ItemManager.instance.getItem(id).iconTexture.getString());
        resize();
    }

    public void setAmount(int amount) {
        this.amount = amount;
        updateAmountText();
    }

    public void removeItem() {
        if(amount <= 0)
            return;
        this.amount--;
        updateAmountText();
    }

    public int getID() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    private void updateItem(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    private void updateAmountText() {
        amountText
                .setText(amount)
                .setVisible(this.amount > 1);
    }

}
