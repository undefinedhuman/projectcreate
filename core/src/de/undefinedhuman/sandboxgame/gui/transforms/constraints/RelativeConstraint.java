package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class RelativeConstraint extends Constraint {

    public RelativeConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        if (isPosition())
            return (int) (guiTransform.parent.getValue(axis) + guiTransform.parent.getValue(getScaleAxis()) * value);
        else return (int) (guiTransform.parent.getValue(axis) * value);
    }

}
