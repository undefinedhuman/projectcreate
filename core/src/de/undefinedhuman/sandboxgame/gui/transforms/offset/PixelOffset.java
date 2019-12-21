package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class PixelOffset extends Offset {

    public PixelOffset(Gui current, Axis axis, float value) {
        super(current, axis, value);
    }

    @Override
    public int getValue(float scale) {
        return (int) (value * scale);
    }

}
