package de.undefinedhuman.sandboxgame.items.type.weapons;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.items.ItemType;
import de.undefinedhuman.sandboxgame.items.type.tools.Tool;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.HashMap;

public class Weapon extends Tool {

    public Vector2 hitboxSize;

    public Weapon() {
        hitboxSize = new Vector2();
    }

    @Override
    public void load(int id, HashMap<String, LineSplitter> settings) {
        super.load(id, settings);
        type = ItemType.WEAPON;
        hitboxSize = Tools.loadVector2(settings,"Hitbox Size", new Vector2());
    }

}
