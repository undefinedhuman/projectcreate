package de.undefinedhuman.projectcreate.core.ecs.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;

import java.util.HashMap;

public class CollisionBlueprint extends ComponentBlueprint {

    private Vector2Setting size = new Vector2Setting("Size", new Vector2(0, 0)), offset = new Vector2Setting("Offset", new Vector2(0, 0));

    public CollisionBlueprint() {
        super(CollisionComponent.class);
        settings.addSettings(size, offset);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new CollisionComponent(size.getValue(), offset.getValue());
    }

    @Override
    public void delete() {}

}
