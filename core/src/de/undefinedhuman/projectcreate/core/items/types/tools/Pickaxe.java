package de.undefinedhuman.projectcreate.core.items.types.tools;

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
        settings.addSettings(speed, strength, range);
    }

}
