package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public abstract class Constraint {

    protected float value;

    protected GuiComponent gui;
    protected Axis axis;

    public Constraint(float value) {
        this.value = value;
    }

    public Constraint setGui(GuiComponent gui) {
        this.gui = gui;
        return this;
    }

    public Constraint setAxis(Axis axis) {
        this.axis = axis;
        return this;
    }

    public Axis getAxis() { return axis; }
    public Axis getScaleAxis() { return axis == Axis.X ? Axis.WIDTH : Axis.HEIGHT; }
    public void setValue(float value) {
        this.value = value;
    }

    protected boolean isPosition() { return axis == Axis.X || axis == Axis.Y; }

    public abstract int getValue(float guiScale);

}
