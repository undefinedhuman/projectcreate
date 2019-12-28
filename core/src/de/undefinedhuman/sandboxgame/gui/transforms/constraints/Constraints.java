package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.Offset;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.RelativeOffset;

import java.util.HashMap;

public class Constraints {

    private HashMap<Axis, Constraint> constraints = new HashMap<>();
    private HashMap<Axis, Offset> offsets = new HashMap<>();
    private GuiComponent gui;

    public Constraints() {}

    public Constraints setGui(GuiComponent gui) {
        this.gui = gui;
        for(Constraint constraint : constraints.values()) constraint.setGui(gui);
        for(Offset offset : offsets.values()) offset.setGui(gui);
        return this;
    }

    public Constraints set(Constraint x, Constraint y, Constraint width, Constraint height) {
        this.constraints.put(Axis.X, x.setAxis(Axis.X).setGui(gui));
        this.constraints.put(Axis.Y, y.setAxis(Axis.Y).setGui(gui));
        this.constraints.put(Axis.WIDTH, width.setAxis(Axis.WIDTH).setGui(gui));
        this.constraints.put(Axis.HEIGHT, height.setAxis(Axis.HEIGHT).setGui(gui));
        return this;
    }

    public Constraints setPosition(Constraint x, Constraint y) {
        this.constraints.put(Axis.X, x.setAxis(Axis.X).setGui(gui));
        this.constraints.put(Axis.Y, y.setAxis(Axis.Y).setGui(gui));
        return this;
    }

    public Constraints setOffset(Offset x, Offset y) {
        this.offsets.put(Axis.X, x.setAxis(Axis.X).setGui(gui));
        this.offsets.put(Axis.Y, y.setAxis(Axis.Y).setGui(gui));
        return this;
    }

    public Constraints setOffsetX(Offset x) {
        this.offsets.put(Axis.X, x.setAxis(Axis.X).setGui(gui));
        return this;
    }

    public Constraints setOffsetY(Offset y) {
        this.offsets.put(Axis.Y, y.setAxis(Axis.Y).setGui(gui));
        return this;
    }

    public Constraints setConstraint(Axis axis, Constraint constraint) {
        this.constraints.put(axis, constraint.setAxis(axis).setGui(gui));
        return this;
    }

    public Constraints setOffset(Axis axis, Offset offset) {
        this.offsets.put(axis, offset.setAxis(axis).setGui(gui));
        return this;
    }

    public Constraints setCentered() {
        this.offsets.put(Axis.X, new RelativeOffset(-0.5f).setAxis(Axis.X).setGui(gui));
        this.offsets.put(Axis.Y, new RelativeOffset(-0.5f).setAxis(Axis.Y).setGui(gui));
        return this;
    }

    public Constraints setCenteredX() {
        this.offsets.put(Axis.X, new RelativeOffset(-0.5f).setAxis(Axis.X).setGui(gui));
        return this;
    }

    public Constraints setCenteredY() {
        this.offsets.put(Axis.Y, new RelativeOffset(-0.5f).setAxis(Axis.Y).setGui(gui));
        return this;
    }

    public void setValue(Axis axis, float value) {
        if(!constraints.containsKey(axis)) return;
        this.constraints.get(axis).setValue(value);
    }

    public int getValue(Axis axis, float guiScale) {
        if(!constraints.containsKey(axis)) return 0;
        return constraints.get(axis).getValue(guiScale);
    }

    public int getOffset(Axis axis, float guiScale) {
        if(!offsets.containsKey(axis)) return 0;
        return offsets.get(axis).getValue(guiScale);
    }

    public void delete() {
        constraints.clear();
    }

}
