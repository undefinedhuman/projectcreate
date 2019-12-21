package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class RelativeOffset extends Offset {

    public RelativeOffset(Gui current, Axis axis, float value) {
        super(current, axis, value);
    }

    @Override
    public int getValue(float scale) {
        return (int) (current.parent.getBound(isX() ? Axis.WIDTH : Axis.HEIGHT) * value);
    }

}
