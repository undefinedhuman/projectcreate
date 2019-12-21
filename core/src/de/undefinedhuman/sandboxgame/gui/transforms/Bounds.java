package de.undefinedhuman.sandboxgame.gui.transforms;

import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.Offset;

import java.util.HashMap;

public class Bounds {

    private HashMap<Axis, Integer> bounds = new HashMap<>();

    public void setBounds(Constraints constraints, Offset xOffset, Offset yOffset, float scale) {
        bounds.put(Axis.X, constraints.getValue(Axis.X, scale) + xOffset.getValue(scale));
        bounds.put(Axis.Y, constraints.getValue(Axis.Y, scale) + yOffset.getValue(scale));
        bounds.put(Axis.WIDTH, constraints.getValue(Axis.WIDTH, scale));
        bounds.put(Axis.HEIGHT, constraints.getValue(Axis.HEIGHT, scale));
    }

    public void initScreen(float width, float height) {
        bounds.put(Axis.X, 0);
        bounds.put(Axis.Y, 0);
        bounds.put(Axis.WIDTH, (int) width);
        bounds.put(Axis.HEIGHT, (int) height);
    }

    public int getValue(Axis axis) {
        return bounds.get(axis);
    }

}
