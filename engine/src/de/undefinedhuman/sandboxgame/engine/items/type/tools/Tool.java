package de.undefinedhuman.sandboxgame.engine.items.type.tools;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;

import java.util.HashMap;

public class Tool extends Item {

    public Tool() {}

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        type = ItemType.TOOL;
    }

}
