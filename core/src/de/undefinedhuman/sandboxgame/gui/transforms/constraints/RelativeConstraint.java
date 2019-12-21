package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class RelativeConstraint extends Constraint {

    public RelativeConstraint(Axis axis, float value) {
        super(axis, value);
    }

    @Override
    public int getValue() {

        if(isPosition()) return (int) (gui.parent.getBound(axis) + gui.parent.getBound(getScaleAxis()) * value);
        else return (int) (gui.parent.getBound(axis) * value);
    }

}
