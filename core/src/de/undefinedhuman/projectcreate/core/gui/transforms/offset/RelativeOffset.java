package de.undefinedhuman.projectcreate.core.gui.transforms.offset;

public class RelativeOffset extends Offset {

    public RelativeOffset(float value) {
        super(value);
    }

    @Override
    public int getValue(float guiScale) {
        return (int) (currentTransform.getCurrentValue(getScaleAxis()) * value);
    }

}
