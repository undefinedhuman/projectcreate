package de.undefinedhuman.sandboxgame.engine.items.type.tools;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Pickaxe extends Tool {

    public float speed, strength;
    public int range;

    public Pickaxe() {
        this.speed = 0.5f;
        this.strength = 10;
        this.range = 6;
        this.type = ItemType.PICKAXE;
    }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        speed = Tools.loadFloat(settings, "Speed", 0.5f);
        strength = Tools.loadFloat(settings, "Strength", 10);
        range = Tools.loadInt(settings, "BlockRange", 6);
        type = ItemType.PICKAXE;
    }

}
