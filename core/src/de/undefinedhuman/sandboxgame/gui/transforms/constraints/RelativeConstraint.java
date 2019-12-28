package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class RelativeConstraint extends Constraint {

    public RelativeConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        if(isPosition()) return (int) (gui.getParentBounds().getValue(axis) + gui.getParentBounds().getValue(getScaleAxis()) * value);
        else return (int) (gui.getParentBounds().getValue(axis) * value);
    }

}
