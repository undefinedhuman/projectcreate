package de.undefinedhuman.sandboxgame.engine.items.type.weapons;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.settings.Vector2Setting;

public class Weapon extends Item {

    public Vector2Setting hitboxSize = new Vector2Setting("Hitbox", new Vector2(0, 0));

    public Weapon() {
        super();
        settings.addSettings(hitboxSize);
    }

}
