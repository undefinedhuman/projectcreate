package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.GuiTransform;

public abstract class Constraint {

    protected float value;

    protected GuiTransform guiTransform;
    protected Axis axis;

    public Constraint(float value) {
        this.value = value;
    }

    public Constraint setGui(GuiTransform guiTransform) {
        this.guiTransform = guiTransform;
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
    public float getValue() { return value; }

    protected boolean isPosition() { return axis == Axis.X || axis == Axis.Y; }

    public abstract int getValue(float guiScale);

}
