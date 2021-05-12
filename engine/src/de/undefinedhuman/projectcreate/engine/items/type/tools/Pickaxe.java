package de.undefinedhuman.projectcreate.engine.items.type.tools;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class Pickaxe extends Tool {

    public Setting
            speed = new Setting("Speed", 0.5f),
            strength = new Setting("Strength", 10f),
            range = new Setting("Range", 6);

    public Pickaxe() {
        super();
        settings.addSettings(speed, strength, range);
    }

}
