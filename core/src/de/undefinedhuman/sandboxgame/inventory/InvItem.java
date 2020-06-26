package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.item.ItemManager;

public class InvItem extends Gui {

    private int id, amount;
    private Text amountText;

    public InvItem(int id) {
        this(id, 0);
    }

    public InvItem(int id, int amount) {
        super(ItemManager.instance.getItem(id).iconTexture.getString());
        set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE)).setCentered();

        this.id = id;
        this.amount = amount;
        amountText = new Text(amount);
        amountText.setPosition(new RelativeConstraint(0.75f), new RelativeConstraint(0.25f)).setCentered();
        amountText.parent = this;

    }

    public void setStats(int id, int amount) {
        this.id = id;
        this.amount = amount;
        amountText.setText(amount);
        setTexture(ItemManager.instance.getItem(id).iconTexture.getString());
    }

    public int getID() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        amountText.setText(amount);
    }

    public void removeItem() {
        if (this.amount > 0) this.amount--;
        amountText.setText(amount);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        amountText.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        if (amount > 1) amountText.render(batch, camera);
    }

    @Override
    public GuiComponent setPosition(int x, int y) {
        super.setPosition(x, y);
        amountText.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

}
