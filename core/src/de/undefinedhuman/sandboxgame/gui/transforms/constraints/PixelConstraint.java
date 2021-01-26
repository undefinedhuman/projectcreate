package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.Gui;

public class PixelConstraint extends Constraint {

    public PixelConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        return (int) ((isPosition() ? currentTransform.parent.getCurrentValue(axis) + (currentTransform.parent instanceof Gui ? ((Gui) currentTransform.parent).getCornerSize() : 0) : 0) + (value * scale));
    }

}
