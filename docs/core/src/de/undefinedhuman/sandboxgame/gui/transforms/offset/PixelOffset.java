package de.undefinedhuman.sandboxgame.gui.transforms.offset;

public class PixelOffset extends Offset {

    public PixelOffset(int value) {
        super(value);
    }

    @Override
    public int getValue(float guiScale) {
        return (int) (value * guiScale);
    }

}
