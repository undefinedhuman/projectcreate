package de.undefinedhuman.projectcreate.engine.items.type.weapons;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class Sword extends Weapon {

    public Setting
            damage = new Setting("Damage", 5f),
            speed = new Setting("Speed", 1f);

    public Sword() {
        super();
        settings.addSettings(damage, speed);
    }

}
