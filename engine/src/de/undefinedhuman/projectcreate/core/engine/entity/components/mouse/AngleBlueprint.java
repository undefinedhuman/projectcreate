package de.undefinedhuman.projectcreate.core.engine.entity.components.mouse;

import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;

import java.util.HashMap;

public class AngleBlueprint extends ComponentBlueprint {

    public AngleBlueprint() {
        this.type = ComponentType.ANGLE;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new AngleComponent();
    }

    @Override
    public void delete() {}

}
