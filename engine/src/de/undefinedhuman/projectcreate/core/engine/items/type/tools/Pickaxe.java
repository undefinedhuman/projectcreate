package de.undefinedhuman.projectcreate.core.engine.items.type.tools;

import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

public class Pickaxe extends Tool {

    public Setting
            speed = new Setting(SettingType.Float, "Speed", 0.5f),
            strength = new Setting(SettingType.Float, "Strength", 10f),
            range = new Setting(SettingType.Int, "Range", 6);

    public Pickaxe() {
        super();
        settings.addSettings(speed, strength, range);
    }

}
