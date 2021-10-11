package de.undefinedhuman.projectcreate.core.ecs.player.combat;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;

public class CombatBlueprint extends ComponentBlueprint {

    @Override
    public Component createInstance() {
        return new CombatComponent();
    }

}
