package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class RelativeOffset extends Offset {

    public RelativeOffset(float value) {
        super(value);
    }

    @Override
    public int getValue(float guiScale) {
        return (int) (gui.getBounds().getValue(isX() ? Axis.WIDTH : Axis.HEIGHT) * value);
    }

}
