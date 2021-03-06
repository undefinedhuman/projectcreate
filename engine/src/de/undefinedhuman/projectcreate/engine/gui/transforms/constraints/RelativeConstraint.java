package de.undefinedhuman.projectcreate.engine.gui.transforms.constraints;

public class RelativeConstraint extends Constraint {

    private int offset;

    public RelativeConstraint(float value) {
        this(value, 0);
    }

    public RelativeConstraint(float value, int offset) {
        super(value);
        this.offset = offset;
    }

    @Override
    public int getValue(float scale) {
        if(isPosition())
            return (int) Math.ceil(currentTransform.parent.getCurrentValue(axis) + currentTransform.parent.getScaledCornerSize() + (currentTransform.parent.getCurrentValue(getScaleAxis()) - currentTransform.parent.getScaledCornerSize() * 2) * value + offset * scale);
        else return (int) Math.ceil((currentTransform.parent.getCurrentValue(axis) - currentTransform.parent.getScaledCornerSize() * 2) * value + offset * scale);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
