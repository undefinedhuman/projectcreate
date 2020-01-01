package de.undefinedhuman.sandboxgame.items.type.Armor;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.items.ItemType;

import java.util.HashMap;

public class Helmet extends Armor {

    public Helmet() { }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        type = ItemType.HELMET;
    }

}
