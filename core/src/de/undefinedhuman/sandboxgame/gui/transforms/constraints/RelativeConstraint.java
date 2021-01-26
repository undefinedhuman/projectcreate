package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.Gui;

public class RelativeConstraint extends Constraint {

    public RelativeConstraint(float value) {
        super(value);
    }

    @Override
    public int getValue(float scale) {
        if(currentTransform.parent instanceof Gui) {
            Gui parentGui = (Gui) currentTransform.parent;
            if(isPosition())
                return (int) (parentGui.getCurrentValue(axis) + parentGui.getCornerSize() + (parentGui.getCurrentValue(getScaleAxis()) - parentGui.getCornerSize() * 2) * value);
            else return (int) ((parentGui.getCurrentValue(axis) - parentGui.getCornerSize() * 2) * value);
        } else {
            if (isPosition())
                return (int) (currentTransform.parent.getCurrentValue(axis) + currentTransform.parent.getCurrentValue(getScaleAxis()) * value);
            else return (int) (currentTransform.parent.getCurrentValue(axis) * value);
        }
    }

}
