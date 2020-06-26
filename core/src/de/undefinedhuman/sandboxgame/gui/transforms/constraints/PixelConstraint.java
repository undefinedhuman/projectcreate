package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class PixelConstraint extends Constraint {

    public PixelConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        return (int) ((isPosition() ? currentTransform.parent.getCurrentValue(axis) : 0) + (value * scale));
    }

}
