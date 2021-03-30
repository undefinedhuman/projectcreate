package de.undefinedhuman.projectcreate.engine.items.type.weapons;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

public class Sword extends Weapon {

    public Setting
            damage = new Setting(SettingType.Float, "Damage", 5f),
            speed = new Setting(SettingType.Float, "Speed", 1f);

    public Sword() {
        super();
        settings.addSettings(damage, speed);
    }

}
