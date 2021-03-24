package de.undefinedhuman.projectcreate.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.inventory.slot.InvSlot;

public interface InvTarget {
    InvSlot getClickedSlot(OrthographicCamera camera);
}
