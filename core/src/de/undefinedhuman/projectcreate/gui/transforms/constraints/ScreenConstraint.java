package de.undefinedhuman.projectcreate.gui.transforms.constraints;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.projectcreate.gui.transforms.Axis;

public class ScreenConstraint extends Constraint {

    public ScreenConstraint() {
        super(0);
    }

    @Override
    public int getValue(float scale) {
        return axis == Axis.WIDTH ? Gdx.graphics.getWidth() : axis == Axis.HEIGHT ? Gdx.graphics.getHeight() : 0;
    }

}
