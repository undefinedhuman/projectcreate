package de.undefinedhuman.projectcreate.core.engine.items.type.weapons;

import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

public class Sword extends Weapon {

    public Setting
            damage = new Setting(SettingType.Float, "Damage", 5f),
            speed = new Setting(SettingType.Float, "Speed", 1f);

    public Sword() {
        super();
        settings.addSettings(damage, speed);
    }

}
