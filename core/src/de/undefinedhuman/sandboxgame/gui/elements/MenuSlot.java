package de.undefinedhuman.sandboxgame.gui.elements;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.CenterOffset;

public abstract class MenuSlot extends Gui {

    private boolean selected = false;

    public MenuSlot(String iconPreview, Constraint x, Constraint y) {
        super(GuiTemplate.SLOT);
        set(x, y, new PixelConstraint(Variables.SLOT_SIZE), new PixelConstraint(Variables.SLOT_SIZE));
        addChild(new Gui(iconPreview).set(new CenterConstraint(), new CenterConstraint(), new PixelConstraint(Variables.ITEM_SIZE), new PixelConstraint(Variables.ITEM_SIZE)).setOffset(new CenterOffset(), new CenterOffset()));
        addEvent(new ClickEvent() {
            @Override
            public void onClick() {
                MenuSlot.this.onClick();
            }
        });
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

    public abstract void onClick();

}
