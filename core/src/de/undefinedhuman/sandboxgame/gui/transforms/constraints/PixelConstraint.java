package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class PixelConstraint extends Constraint {

    public PixelConstraint(Axis axis, float value) {
        super(axis, value);
    }

    @Override
    public int getValue() {
        return (int) (value + (isPosition() ? axis == Axis.X ? gui.parent.getBound(Axis.X) : gui.parent.getBound(Axis.Y) : 0));
    }

}
