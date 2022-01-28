package de.undefinedhuman.projectcreate.core.ecs.base.transform;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.base.Transform;
import de.undefinedhuman.projectcreate.engine.ecs.Component;

public class TransformComponent extends Transform implements Component {

    public TransformComponent(Vector2 size) {
        super(size);
    }

}
