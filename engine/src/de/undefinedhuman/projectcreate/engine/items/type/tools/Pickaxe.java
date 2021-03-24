package de.undefinedhuman.projectcreate.engine.items.type.tools;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

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
