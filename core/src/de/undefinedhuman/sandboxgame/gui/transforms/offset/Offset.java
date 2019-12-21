package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public abstract class Offset {

    protected Gui current;
    protected float value;

    private Axis axis;

    public Offset(Gui current, Axis axis, float value) {
        this.current = current;
        this.axis = axis;
        this.value = value;
    }

    public boolean isX() {
        return axis == Axis.X;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public abstract int getValue(float scale);

}
