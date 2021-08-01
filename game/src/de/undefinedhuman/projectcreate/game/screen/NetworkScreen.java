package de.undefinedhuman.projectcreate.game.screen;

import com.badlogic.gdx.Screen;
import de.undefinedhuman.projectcreate.game.network.ClientManager;

public class NetworkScreen implements Screen {

    public NetworkScreen() {
        ClientManager.getInstance();
    }

    @Override
    public void show() {
        ClientManager.getInstance().connect();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
