package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

import java.util.HashMap;

public class Constraints {

    private HashMap<Axis, Constraint> constraints = new HashMap<>();
    private Gui gui;

    public Constraints() { }
    public Constraints(Constraint x, Constraint y, Constraint width, Constraint height) {
        set(x, y, width, height);
    }

    public Constraints setGui(Gui gui) {
        this.gui = gui;
        for(Constraint constraint : constraints.values()) constraint.setGui(gui);
        return this;
    }

    public void set(Constraint x, Constraint y, Constraint width, Constraint height) {
        this.constraints.put(Axis.X, x.setGui(gui));
        this.constraints.put(Axis.Y, y.setGui(gui));
        this.constraints.put(Axis.WIDTH, width.setGui(gui));
        this.constraints.put(Axis.HEIGHT, height.setGui(gui));
    }

    public Constraints setPixelConstraint(Axis axis, float value) {
        this.constraints.put(axis, new PixelConstraint(axis, value).setGui(gui));
        return this;
    }

    public Constraints setRelativeConstraint(Axis axis, float value) {
        this.constraints.put(axis, new RelativeConstraint(axis, value).setGui(gui));
        return this;
    }

    public void setValue(Axis axis, float value) { this.constraints.get(axis).setValue(value); }
    public int getValue(Axis axis) { return constraints.get(axis).getValue(); }
    public int getValue(Axis axis, float localScale) { return (int) (constraints.get(axis).getValue() * localScale); }
    public Vector2 getPosition() { return new Vector2(getValue(Axis.X), getValue(Axis.Y)); }
    public Vector2 getPosition(float localScale) { return new Vector2(getValue(Axis.X, localScale), getValue(Axis.Y, localScale)); }
    public Vector2 getScale() { return new Vector2(getValue(Axis.WIDTH), getValue(Axis.HEIGHT)); }
    public Vector2 getScale(float localScale) { return new Vector2(getValue(Axis.WIDTH, localScale), getValue(Axis.HEIGHT, localScale)); }

    public void delete() {
        constraints.clear();
    }

}
