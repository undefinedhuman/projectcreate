package de.undefinedhuman.projectcreate.engine.entity.components.stats.mana;

import de.undefinedhuman.projectcreate.engine.entity.Component;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;

import java.util.HashMap;

public class ManaBlueprint extends ComponentBlueprint {

    public Setting maxMana = new Setting("Max Mana", 0);

    public ManaBlueprint() {
        settings.addSettings(maxMana);
        this.type = ComponentType.MANA;
    }

    @Override
    public Component createInstance(HashMap<ComponentType, ComponentParam> params) {
        return new ManaComponent(maxMana.getInt());
    }

    @Override
    public void delete() {}

}
