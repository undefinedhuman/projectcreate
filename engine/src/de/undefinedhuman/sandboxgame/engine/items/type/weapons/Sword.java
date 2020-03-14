package de.undefinedhuman.sandboxgame.engine.items.type.weapons;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

public class Sword extends Weapon {

    public Setting
            damage = new Setting(SettingType.Float, "Damage", 5f),
            attackSpeed = new Setting(SettingType.Float, "Attack Speed", 1f);

    public Sword() {
        super();
        settings.addSettings(damage, attackSpeed);
    }

}
