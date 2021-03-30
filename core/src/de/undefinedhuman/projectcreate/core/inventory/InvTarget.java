package de.undefinedhuman.projectcreate.core.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.undefinedhuman.projectcreate.core.inventory.slot.InvSlot;

public interface InvTarget {
    InvSlot getClickedSlot(OrthographicCamera camera);
}
