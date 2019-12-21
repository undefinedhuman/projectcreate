package de.undefinedhuman.sandboxgame.gui.transforms.constraints;

import de.undefinedhuman.sandboxgame.gui.transforms.Axis;

public class ConstraintFactory {

    public static Constraints setPixelConstraints(float x, float y, float width, float height) {
        return new Constraints().setPixelConstraint(Axis.X, x).setPixelConstraint(Axis.Y, y).setPixelConstraint(Axis.WIDTH, width).setPixelConstraint(Axis.HEIGHT, height);
    }

    public static Constraints setRelativeConstraints(float x, float y, float width, float height) {
        return new Constraints().setRelativeConstraint(Axis.X, x).setRelativeConstraint(Axis.Y, y).setRelativeConstraint(Axis.WIDTH, width).setRelativeConstraint(Axis.HEIGHT, height);
    }

}
