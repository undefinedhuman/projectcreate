package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;

public class PixelConstraint extends Constraint {

    public PixelConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        GuiTransform parent = currentTransform.parent;
        return (int) ((isPosition() ? parent.getCurrentValue(axis) + (parent instanceof Gui ? ((Gui) parent).getCornerSize() : 0) : 0) + (value * scale));
    }

}
