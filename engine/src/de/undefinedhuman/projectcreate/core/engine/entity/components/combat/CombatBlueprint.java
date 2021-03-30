package de.undefinedhuman.projectcreate.core.engine.entity.components.combat;

import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;

import java.util.HashMap;

public class CombatBlueprint extends ComponentBlueprint {

    public CombatBlueprint() {
        this.type = ComponentType.COMBAT;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new CombatComponent();
    }

}
