package de.undefinedhuman.projectcreate.engine.gui.transforms.constraints;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class MouseConstraint extends Constraint {

    private OrthographicCamera camera;

    public MouseConstraint(OrthographicCamera camera) {
        super(0);
        this.camera = camera;
    }

    @Override
    public int getValue(float scale) {
        // TODO Refactor that calculation takes place only once per frame
        Vector2 mousePosition = Utils.calculateWorldFromScreenSpace(camera, Gdx.input.getX(), Gdx.input.getY());
        return (int) (axis == Axis.X ? mousePosition.x : mousePosition.y);
    }

}
