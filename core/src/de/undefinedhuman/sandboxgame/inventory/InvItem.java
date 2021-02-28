package de.undefinedhuman.sandboxgame.inventory;

import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.sandboxgame.item.ItemManager;

public class InvItem extends Gui {

    private int id, amount;
    private Text amountText;

    public InvItem(int id) {
        this(id, 0);
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

    public void setStats(int id, int amount) {
        updateItem(id, amount);
        updateAmountText();
        setTexture(ItemManager.instance.getItem(id).iconTexture.getString());
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
