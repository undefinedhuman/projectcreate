package de.undefinedhuman.projectcreate.core.inventory.slot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.Main;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.Constraint;
import de.undefinedhuman.projectcreate.core.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.core.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.gui.GuiComponent;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTexture;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTextureManager;
import de.undefinedhuman.projectcreate.core.gui.transforms.Axis;

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
