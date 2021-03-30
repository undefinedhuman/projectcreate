package de.undefinedhuman.projectcreate.core.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import de.undefinedhuman.projectcreate.core.utils.Inputs;

public class GameScreen implements Screen {

    public static GameScreen instance;

    public GameScreen() {
        GameManager.instance = new GameManager();
    }

    @Override
    public void show() {
        GameManager.instance.init();
        Gdx.graphics.setResizable(true);
        Gdx.input.setInputProcessor(Inputs.instance);
    }

    @Override
    public void render(float delta) {
        GameManager.instance.update(delta);
        GameManager.instance.render();
    }

    @Override
    public void resize(int width, int height) {
        GameManager.instance.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        GameManager.instance.delete();
    }

}
