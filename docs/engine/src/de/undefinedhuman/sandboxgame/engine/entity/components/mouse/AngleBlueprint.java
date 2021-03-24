package de.undefinedhuman.sandboxgame.engine.entity.components.mouse;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;

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
