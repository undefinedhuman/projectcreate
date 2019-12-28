package de.undefinedhuman.sandboxgame.gui.transforms.offset;

import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public abstract class Offset {

    protected float value;
    protected GuiComponent gui;
    private Axis axis;

    public Offset(float value) {
        this.value = value;
    }

    public Offset setAxis(Axis axis) {
        this.axis = axis;
        return this;
    }

    public Offset setGui(GuiComponent gui) {
        this.gui = gui;
        return this;
    }

    public Axis getAxis() { return axis; }
    public boolean isX() { return axis == Axis.X; }
    public void setValue(float value) { this.value = value; }

    public abstract int getValue(float guiScale);

}
