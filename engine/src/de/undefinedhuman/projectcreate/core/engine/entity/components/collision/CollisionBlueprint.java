package de.undefinedhuman.projectcreate.core.engine.entity.components.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.types.Vector2Setting;

import java.util.HashMap;

public class CollisionBlueprint extends ComponentBlueprint {

    private Setting size = new Vector2Setting("Size", new Vector2(0, 0)), offset = new Vector2Setting("Offset", new Vector2(0, 0));

    public CollisionBlueprint() {
        settings.addSettings(size, offset);
        this.type = ComponentType.COLLISION;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new CollisionComponent(size.getVector2(), offset.getVector2());
    }

    @Override
    public void delete() {}

}
