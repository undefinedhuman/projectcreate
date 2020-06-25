package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class RelativeConstraint extends Constraint {

    public RelativeConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        if (isPosition())
            return (int) (currentTransform.parent.getValue(axis) + currentTransform.parent.getValue(getScaleAxis()) * value);
        else return (int) (currentTransform.parent.getValue(axis) * value);
    }

}
