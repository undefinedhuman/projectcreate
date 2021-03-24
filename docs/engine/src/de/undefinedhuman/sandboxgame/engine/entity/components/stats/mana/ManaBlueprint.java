package de.undefinedhuman.sandboxgame.engine.entity.components.stats.mana;

import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

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
