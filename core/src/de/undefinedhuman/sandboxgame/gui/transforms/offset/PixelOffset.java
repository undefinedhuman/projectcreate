package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraint;

public class PixelOffset extends Constraint {

    public PixelOffset(float value) {
        super(value);
    }

    @Override
    public int getValue(float guiScale) {
        return (int) (value * guiScale);
    }

}
