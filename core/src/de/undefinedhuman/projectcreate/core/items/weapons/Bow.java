package de.undefinedhuman.projectcreate.core.items.weapons;

import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class Bow extends Weapon {

    public IntSetting
            range = new IntSetting("Launcher Angle", 100),
            strength = new IntSetting("Launcher Distance", 18);

    public Bow() {
        super();
        type = ItemType.BOW;
        addSettings(range, strength);
        addSettingsGroup(new SettingsGroup("Bow", range, strength));
    }

}
