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
    private int selectedSize = 0;
    private int selectedOffset = 0;

    public Slot(Constraint x, Constraint y) {
        super();
        set(x, y, new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
    }

    @Override
    public void resize(int width, int height) {
        selectedSize = Variables.SELECTED_SIZE * GuiManager.GUI_SCALE;
        selectedOffset = -(selectedSize / 2);
        super.resize(width, height);
        GuiTextureManager.getInstance()
                .addGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH), getCurrentValue(Axis.HEIGHT))
                .addGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH) + selectedSize, getCurrentValue(Axis.HEIGHT) + selectedSize);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        GuiTexture texture = GuiTextureManager.getInstance().getGuiTexture(GuiTemplate.SLOT, getCurrentValue(Axis.WIDTH) + (selected ? selectedSize : 0), getCurrentValue(Axis.HEIGHT) + (selected ? selectedSize : 0));
        if(texture == null)
            return;
        int offset = selected ? selectedOffset : 0;
        texture.render(batch, getCurrentValue(Axis.X) + offset, getCurrentValue(Axis.Y) + offset, alpha);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        resize();
    }

    public boolean isSelected() {
        return selected;
    }

}
