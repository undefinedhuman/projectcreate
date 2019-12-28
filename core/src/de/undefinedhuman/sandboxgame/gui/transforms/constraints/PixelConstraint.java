package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class PixelConstraint extends Constraint {

    public PixelConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        return (int) ((int) (value + (isPosition() ? gui.getParentBounds().getValue(axis) : 0)) * scale);
    }

}
