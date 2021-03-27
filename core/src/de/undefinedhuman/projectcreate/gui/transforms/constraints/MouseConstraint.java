package de.undefinedhuman.projectcreate.gui.transforms.constraints;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.utils.Utils;

public class MouseConstraint extends Constraint {

    private OrthographicCamera camera;

    public MouseConstraint(OrthographicCamera camera) {
        super(0);
        this.camera = camera;
    }

    @Override
    public int getValue(float scale) {
        // TODO Refactor that calculation takes place only once per frame
        Vector2 mousePosition = Utils.getMouseCoordsInWorldSpace(camera);
        return (int) (axis == Axis.X ? mousePosition.x : mousePosition.y);
    }

}
