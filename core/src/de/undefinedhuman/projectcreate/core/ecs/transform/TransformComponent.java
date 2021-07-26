package de.undefinedhuman.projectcreate.core.ecs.transform;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.base.Transform;

public class TransformComponent extends Transform implements Component {

    public TransformComponent(Vector2 size) {
        super(size);
    }

}
