package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

public class ConstantConstraint extends Constraint {

    public ConstantConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        return (int) value;
    }

}
