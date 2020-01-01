package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface InvTarget {

    Slot getClickedSlot(OrthographicCamera camera);

}
