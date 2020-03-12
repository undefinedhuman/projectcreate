package de.undefinedhuman.sandboxgame.engine.items.type.Armor;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Armor extends Item {

    public float armor;
    public String[] inVisibleSprites;

    public Armor() {
        this.armor = 0;
        this.inVisibleSprites = new String[0];
    }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        armor = Tools.loadFloat(settings, "Armor", 0);
        inVisibleSprites = Tools.loadStringArray(settings, "Invisible Sprites", "");
        type = ItemType.ARMOR;
    }

}
