package de.undefinedhuman.sandboxgame.items.type.weapons;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.items.ItemType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Staff extends Weapon {

    public float launcherAngle, launcherDistance;

    public Staff() {
        this.launcherDistance = 63;
        this.launcherAngle = -18;
    }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        launcherAngle = Tools.loadFloat(settings, "Launcher Angle", -18);
        launcherDistance = Tools.loadFloat(settings, "Launcher Distance", 63);
        type = ItemType.BOW;
    }

}
