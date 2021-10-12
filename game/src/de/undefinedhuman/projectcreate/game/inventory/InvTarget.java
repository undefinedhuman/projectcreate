package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface InvTarget {
    ClientInvSlot getClickedSlot(OrthographicCamera camera);
}
