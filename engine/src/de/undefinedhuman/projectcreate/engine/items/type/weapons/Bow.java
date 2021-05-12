package de.undefinedhuman.projectcreate.engine.items.type.weapons;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class Bow extends Weapon {

    public Setting
            launcherAngle = new Setting("Launcher Angle", -18),
            launcherDistance = new Setting("Launcher Distance", 63);

    public Bow() {
        super();
        settings.addSettings(launcherAngle, launcherDistance);
    }

}
