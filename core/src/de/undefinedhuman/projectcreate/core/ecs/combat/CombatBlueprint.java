package de.undefinedhuman.projectcreate.core.ecs.combat;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;

import java.util.HashMap;

public class CombatBlueprint extends ComponentBlueprint {

    public CombatBlueprint() {
        super(CombatComponent.class);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new CombatComponent();
    }

}
