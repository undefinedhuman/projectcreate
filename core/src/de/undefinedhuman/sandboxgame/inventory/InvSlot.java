package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;

public class InvSlot extends Slot {

    private boolean selected = false;

    public InvSlot() {
        super();
        setSize(new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
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
        updateSelected(selected);
        return super.setVisible(visible);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateSelected(selected);
        if (invItem != null) invItem.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void updateSelected(boolean selected) {
        int slotScale = Variables.SLOT_SIZE + (selected ? Variables.SELECTED_AMOUNT : 0);
        setSize(new PixelConstraint(slotScale), new PixelConstraint(slotScale));
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        int offset = -(selected ? (Variables.SELECTED_AMOUNT / 2) : 0) * Main.guiScale;
        texture.resize(getCurrentValue(Axis.X) + offset, getCurrentValue(Axis.Y) + offset, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
    }

}
