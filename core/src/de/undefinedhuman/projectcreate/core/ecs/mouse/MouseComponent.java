package de.undefinedhuman.projectcreate.core.ecs.mouse;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MouseComponent implements Component {
    public Vector2 mousePosition = new Vector2();
    public boolean isLeftMouseButtonDown = false, isRightMouseButtonDown = false, isTurned = false;
    public float angle;
}
