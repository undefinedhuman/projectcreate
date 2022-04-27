package de.undefinedhuman.projectcreate.core.items.tools;

import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class Pickaxe extends Tool {

    public FloatSetting
            speed = new FloatSetting("Speed", 0.5f),
            strength = new FloatSetting("Strength", 10f);
    public IntSetting
            range = new IntSetting("Range", 6);

    public Pickaxe() {
        super();
        addSettings(speed, strength, range);
        addSettingsGroup(new SettingsGroup("Pickaxe", speed, strength, range));
    }

}
