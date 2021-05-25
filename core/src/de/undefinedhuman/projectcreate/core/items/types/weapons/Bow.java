package de.undefinedhuman.projectcreate.core.items.types.weapons;

import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class Bow extends Weapon {

    public IntSetting
            range = new IntSetting("Launcher Angle", 100),
            strength = new IntSetting("Launcher Distance", 18);

    public Bow() {
        super();
        settings.addSettings(range, strength);
    }

}
