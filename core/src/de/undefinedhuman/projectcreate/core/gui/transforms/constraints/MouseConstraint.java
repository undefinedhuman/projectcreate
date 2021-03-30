package de.undefinedhuman.projectcreate.core.gui.transforms.constraints;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.core.utils.Tools;

public class MouseConstraint extends Constraint {

    private OrthographicCamera camera;

    public MouseConstraint(OrthographicCamera camera) {
        super(0);
        this.camera = camera;
    }

    @Override
    public int getValue(float scale) {
        // TODO Refactor that calculation takes place only once per frame
        Vector2 mousePosition = Tools.getMouseCoordsInWorldSpace(camera);
        return (int) (axis == Axis.X ? mousePosition.x : mousePosition.y);
    }

}
