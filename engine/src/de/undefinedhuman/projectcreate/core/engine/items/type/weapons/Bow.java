package de.undefinedhuman.projectcreate.core.engine.items.type.weapons;

import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

public class Bow extends Weapon {

    public Setting
            launcherAngle = new Setting(SettingType.Float, "Launcher Angle", -18),
            launcherDistance = new Setting(SettingType.Float, "Launcher Distance", 63);

    public Bow() {
        super();
        settings.addSettings(launcherAngle, launcherDistance);
    }

}
