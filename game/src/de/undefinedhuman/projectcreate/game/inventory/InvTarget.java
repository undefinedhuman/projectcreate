package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.game.inventory.slot.InvSlot;

public interface InvTarget {
    InvSlot getClickedSlot(OrthographicCamera camera);
}
