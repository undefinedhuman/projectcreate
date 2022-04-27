package de.undefinedhuman.projectcreate.core.items.weapons;

import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class Sword extends Weapon {

    public FloatSetting
            damage = new FloatSetting("Damage", 5f),
            speed = new FloatSetting("Speed", 1f);

    public Sword() {
        super();
        addSettings(damage, speed);
        addSettingsGroup(new SettingsGroup("Sword", damage, speed));
    }

}
