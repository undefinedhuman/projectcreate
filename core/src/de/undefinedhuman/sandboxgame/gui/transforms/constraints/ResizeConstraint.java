package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class ResizeConstraint extends Constraint {

    public ResizeConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        if (isPosition())
            return (int) (currentTransform.parent.getValue(axis) + currentTransform.parent.getValue(getScaleAxis()) * value);
        else return (int) (currentTransform.parent.getValue(axis) * value);
    }

}
