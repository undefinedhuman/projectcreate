package de.undefinedhuman.projectcreate.core.ecs.mana;

import com.badlogic.ashley.core.Component;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class ManaBlueprint extends ComponentBlueprint {

    public IntSetting maxMana = new IntSetting("Max Mana", 0);

    public ManaBlueprint() {
        settings.addSettings(maxMana);
    }

    @Override
    public Component createInstance() {
        return new ManaComponent(maxMana.getValue());
    }

    @Override
    public void delete() {}

}
