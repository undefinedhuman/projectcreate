package de.undefinedhuman.sandboxgame.engine.entity.components.combat;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;

import java.util.HashMap;

public class CombatBlueprint extends ComponentBlueprint {

    public CombatBlueprint() {
        this.type = ComponentType.COMBAT;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new CombatComponent();
    }

    @Override
    public void delete() {

    }

}
