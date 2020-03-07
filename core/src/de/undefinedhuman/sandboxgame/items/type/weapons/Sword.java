package de.undefinedhuman.sandboxgame.items.type.weapons;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.items.ItemType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Sword extends Weapon {

    public float damage, speed;

    public Sword() {
        this.damage = 5;
        this.speed = 1.1f;
        this.type = ItemType.SWORD;
    }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        damage = Tools.loadFloat(settings, "Damage", 5);
        speed = Tools.loadFloat(settings, "Speed", 1.1f);
        type = ItemType.SWORD;
    }

}
