package de.undefinedhuman.sandboxgame.gui.transforms;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;

import java.util.HashMap;

public class Bounds {

    private HashMap<Axis, Integer> bounds = new HashMap<>();

    public void resize(Constraints constraints, float scale) {
        for(Axis axis : Axis.values()) resizeAxis(axis, constraints, scale);
    }

    public void initScreen(float width, float height) {
        bounds.put(Axis.X, 0);
        bounds.put(Axis.Y, 0);
        bounds.put(Axis.WIDTH, (int) width);
        bounds.put(Axis.HEIGHT, (int) height);
    }

    public int getValue(Axis axis) {
        if(!bounds.containsKey(axis)) return 0;
        return bounds.get(axis);
    }

    public void setValue(Axis axis, int value) {
        if(!bounds.containsKey(axis)) return;
        bounds.put(axis, value);
    }

    public void addValue(Axis axis, int value) {
        if(!bounds.containsKey(axis)) return;
        bounds.put(axis, bounds.get(axis) + value);
    }

    public Vector2 getPosition() {
        return new Vector2(getValue(Axis.X), getValue(Axis.Y));
    }
    public Vector2 getScale() {
        return new Vector2(getValue(Axis.WIDTH), getValue(Axis.HEIGHT));
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(Axis axis : bounds.keySet()) s.append(axis.toString()).append(": ").append(bounds.get(axis)).append(", ");
        return s.toString();
    }

    private void resizeAxis(Axis axis, Constraints constraints, float scale) {
        this.bounds.put(axis, constraints.getValue(axis, scale) + constraints.getOffset(axis, scale));
    }

}
