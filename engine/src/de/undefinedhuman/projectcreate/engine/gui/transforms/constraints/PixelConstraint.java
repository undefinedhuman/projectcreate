package de.undefinedhuman.projectcreate.engine.gui.transforms.constraints;

public class PixelConstraint extends Constraint {

    public PixelConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        return (int) Math.ceil((isPosition() ? currentTransform.parent.getCurrentValue(axis) + currentTransform.parent.getScaledCornerSize() : 0) + (value * scale));
    }

}
