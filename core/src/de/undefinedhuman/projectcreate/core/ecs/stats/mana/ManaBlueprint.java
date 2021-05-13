package de.undefinedhuman.projectcreate.core.ecs.stats.mana;

import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

import java.util.HashMap;

public class ManaBlueprint extends ComponentBlueprint {

    public IntSetting maxMana = new IntSetting("Max Mana", 0);

    public ManaBlueprint() {
        super(ManaComponent.class);
        settings.addSettings(maxMana);
    }

    @Override
    public Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params) {
        return new ManaComponent(maxMana.getValue());
    }

    @Override
    public void delete() {}

}
