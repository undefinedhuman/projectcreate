package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import com.badlogic.gdx.Gdx;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class ResizeConstraint extends Constraint {

    public ResizeConstraint() {
        super(0);
    }

    @Override
    public int getValue(float scale) {
        return axis == Axis.WIDTH ? Gdx.graphics.getWidth() : axis == Axis.HEIGHT ? Gdx.graphics.getHeight() : 0;
    }

}
