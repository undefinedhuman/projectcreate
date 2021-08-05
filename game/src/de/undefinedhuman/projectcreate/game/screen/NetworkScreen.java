package de.undefinedhuman.projectcreate.game.screen;

import de.undefinedhuman.projectcreate.engine.utils.ScreenAdapter;
import de.undefinedhuman.projectcreate.game.network.ClientManager;

public class NetworkScreen extends ScreenAdapter {

    public NetworkScreen() {
        ClientManager.getInstance();
    }

    @Override
    public void show() {
        ClientManager.getInstance().connect();
    }

    @Override
    public void dispose() {
        ClientManager.getInstance().delete();
    }
}
