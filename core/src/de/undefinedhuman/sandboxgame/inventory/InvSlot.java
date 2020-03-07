package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class InvSlot extends Slot {

    private boolean selected = false;
    private int off = 0;

    public InvSlot() {
        super();
        setScale("p" + Variables.SLOT_SIZE, "p" + Variables.SLOT_SIZE);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        super.render(batch, camera);

        if (invItem != null) {
            if (invItem.getAmount() > 0) invItem.render(batch, camera);
            else this.deleteItem();
        }

    }

    @Override
    public GuiTransform setVisible(boolean visible) {
        setScale(selected);
        return super.setVisible(visible);
    }

    private void setScale(boolean selected) {
        int slotScale = Variables.SLOT_SIZE + (selected ? Variables.SELECTED_AMOUNT : 0);
        setScale(slotScale, slotScale);
        setScale("p" + slotScale, "p" + slotScale);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        off = (int) (-(selected ? (Variables.SELECTED_AMOUNT / 2) : 0) * guiScale);
        position.add(off, off);
        texture.resize((int) position.x, (int) position.y, (int) scale.x, (int) scale.y, guiScale);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setScale(selected);
        if (invItem != null) invItem.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}
