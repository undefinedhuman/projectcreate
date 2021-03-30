package de.undefinedhuman.projectcreate.engine.entity.components.mouse;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;

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
