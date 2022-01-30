package de.undefinedhuman.projectcreate.core.ecs.player.combat;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;

public class CombatBlueprint extends ComponentBlueprint {

    @Override
    public Component createInstance() {
        return new CombatComponent();
    }

}
