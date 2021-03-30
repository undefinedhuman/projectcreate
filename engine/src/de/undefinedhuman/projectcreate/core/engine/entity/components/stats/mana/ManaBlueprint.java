package de.undefinedhuman.projectcreate.core.engine.entity.components.stats.mana;

import de.undefinedhuman.projectcreate.core.engine.entity.Component;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

import java.util.HashMap;

public class ManaBlueprint extends ComponentBlueprint {

    public Setting maxMana = new Setting(SettingType.Int, "Max Mana", 0);

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
