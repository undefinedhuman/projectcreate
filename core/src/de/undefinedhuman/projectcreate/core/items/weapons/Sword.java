package de.undefinedhuman.projectcreate.core.items.weapons;

import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class Sword extends Weapon {

    public FloatSetting
            damage = new FloatSetting("Damage", 5f),
            speed = new FloatSetting("Speed", 1f);

    public Sword() {
        super();
        settings.addSettings(damage, speed);
    }

}
