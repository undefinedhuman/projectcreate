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
        if (invItem == null) return;
        invItem.render(batch, camera);
    }

    @Override
    public GuiTransform setVisible(boolean visible) {
        updateSelected(selected);
        return super.setVisible(visible);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateSelected(selected);
    }

    public boolean isSelected() {
        return selected;
    }

    private void updateSelected(boolean selected) {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        int selectedSize = (selected ? Variables.SELECTED_AMOUNT : 0) * Main.guiScale, selectedOffset = -(selected ? (Variables.SELECTED_AMOUNT / 2) : 0) * Main.guiScale;
        texture.resize(getCurrentValue(Axis.X) + selectedOffset, getCurrentValue(Axis.Y) + selectedOffset, getCurrentValue(Axis.WIDTH) + selectedSize, getCurrentValue(Axis.HEIGHT) + selectedSize);
    }

}
