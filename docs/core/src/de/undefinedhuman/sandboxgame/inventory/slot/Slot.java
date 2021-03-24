package de.undefinedhuman.sandboxgame.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTextureManager;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;

public class Slot extends GuiComponent {

    private boolean selected = false;

    public Slot(Constraint x, Constraint y) {
        super();
        set(x, y, new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
    }

    @Override
    public void resize(int width, int height) {
        removeGuiTextureFromManager();
        int size = Variables.SLOT_SIZE + (selected ? Variables.SELECTED_SIZE : 0);
        setSize(size, size);
        super.resize(width, height);
        GuiTextureManager.instance.addGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        GuiTexture texture = GuiTextureManager.instance.getGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
        int offset = -(selected ? (Variables.SELECTED_SIZE / 2) : 0) * Main.guiScale;
        if(texture != null)
            texture.render(batch, getCurrentValue(Axis.X) + offset, getCurrentValue(Axis.Y) + offset, alpha);
    }

    @Override
    public void delete() {
        super.delete();
        removeGuiTextureFromManager();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        resize();
    }

    public boolean isSelected() {
        return selected;
    }

    private void removeGuiTextureFromManager() {
        if(getCurrentValue(Axis.WIDTH) != 0 || getCurrentValue(Axis.HEIGHT) != 0)
            GuiTextureManager.instance.removeGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
    }

}
