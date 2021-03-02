package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.sandboxgame.inventory.slot.InvSlot;

public interface InvTarget {
    InvSlot getClickedSlot(OrthographicCamera camera);
}
