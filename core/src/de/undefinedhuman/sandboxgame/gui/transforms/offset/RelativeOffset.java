package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;

public class RelativeOffset extends Constraint {

    public RelativeOffset(float value) {
        super(value);
    }

    @Override
    public int getValue(float guiScale) {
        return (int) (guiTransform.getValue(getScaleAxis()) * value);
    }

}
