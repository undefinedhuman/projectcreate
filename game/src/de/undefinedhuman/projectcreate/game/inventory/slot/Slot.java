package de.undefinedhuman.projectcreate.game.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.gui.GuiComponent;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTexture;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTextureManager;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class Slot extends GuiComponent {

    private boolean selected = false;

    public Slot() {
        super();
        setSize(new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
    }

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
        GuiTextureManager.getInstance().addGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        GuiTexture texture = GuiTextureManager.getInstance().getGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
        int offset = -(selected ? (Variables.SELECTED_SIZE / 2) : 0) * GuiManager.GUI_SCALE;
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
            GuiTextureManager.getInstance().removeGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT));
    }

}
