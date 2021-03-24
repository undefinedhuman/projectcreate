package de.undefinedhuman.projectcreate.engine.items.type.weapons;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

public class Bow extends Weapon {

    public Setting
            launcherAngle = new Setting(SettingType.Float, "Launcher Angle", -18),
            launcherDistance = new Setting(SettingType.Float, "Launcher Distance", 63);

    public Bow() {
        super();
        settings.addSettings(launcherAngle, launcherDistance);
    }

}
