package de.undefinedhuman.projectcreate.core.ecs.transform;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;

public class TransformBlueprint extends ComponentBlueprint {

    public Vector2Setting size = new Vector2Setting("Size", new Vector2(0, 0));

    public TransformBlueprint() {
        addSettings(size);
        priority = ComponentPriority.LOWEST;
    }

    @Override
    public Component createInstance() {
        return new TransformComponent(size.getValue());
    }
}
