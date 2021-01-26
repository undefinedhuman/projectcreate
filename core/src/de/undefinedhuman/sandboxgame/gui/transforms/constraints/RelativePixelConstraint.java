package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.gui.Gui;

public class RelativePixelConstraint extends Constraint {

    private int offset;

    public RelativePixelConstraint(float value, int offset) {
        super(value);
        this.offset = offset;
    }

    @Override
    public int getValue(float scale) {
        if(isPosition())
            Log.error("Relative Pixel Constraint is currently limited to size constraints only!");
        if(currentTransform.parent instanceof Gui) {
            Gui parentGui = (Gui) currentTransform.parent;
            return (int) ((parentGui.getCurrentValue(axis) - parentGui.getCornerSize() * 2) * value - offset * Main.guiScale);
        } else return (int) (currentTransform.parent.getCurrentValue(axis) * value - offset * Main.guiScale);
    }

}
