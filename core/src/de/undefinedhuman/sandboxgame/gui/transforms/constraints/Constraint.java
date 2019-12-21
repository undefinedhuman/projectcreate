package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public abstract class Constraint {

    protected float value;

    protected Gui gui;
    protected Axis axis;

    public Constraint(Axis axis, float value) {
        this.axis = axis;
        this.value = value;
    }

    public Constraint setGui(Gui gui) {
        this.gui = gui;
        return this;
    }

    public Axis getAxis() {
        return axis;
    }
    public Axis getScaleAxis() { return axis == Axis.X ? Axis.WIDTH : Axis.HEIGHT; }
    public void setValue(float value) {
        this.value = value;
    }

    protected boolean isPosition() { return axis == Axis.X || axis == Axis.Y; }

    public abstract int getValue();

}
