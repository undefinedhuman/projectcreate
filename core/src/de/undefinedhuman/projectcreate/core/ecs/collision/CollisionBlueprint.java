package de.undefinedhuman.projectcreate.core.ecs.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentPriority;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;

public class CollisionBlueprint extends ComponentBlueprint {

    private Vector2Setting
            size = new Vector2Setting("Size", new Vector2(0, 0)),
            offset = new Vector2Setting("Offset", new Vector2(0, 0));

    public CollisionBlueprint() {
        addSettings(size, offset);
        priority = ComponentPriority.HIGH;
    }

    @Override
    public Component createInstance() {
        return new CollisionComponent(size.getValue(), offset.getValue());
    }

}
