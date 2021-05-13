package de.undefinedhuman.projectcreate.core.items.types.weapons;

import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class Bow extends Weapon {

    public IntSetting
            launcherAngle = new IntSetting("Launcher Angle", -18),
            launcherDistance = new IntSetting("Launcher Distance", 63);

    public Bow() {
        super();
        settings.addSettings(launcherAngle, launcherDistance);
    }

}
