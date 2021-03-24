package de.undefinedhuman.sandboxgame.engine.items.type.tools;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

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
