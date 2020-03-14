package de.undefinedhuman.sandboxgame.engine.items.type.weapons;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

public class Sword extends Weapon {

    public Setting
            damage = new Setting(SettingType.Float, "Damage", 5f),
            speed = new Setting(SettingType.Float, "Speed", 1f);

    public Sword() {
        super();
        settings.addSettings(damage, speed);
    }

}
