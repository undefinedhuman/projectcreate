package de.undefinedhuman.projectcreate.game.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import de.undefinedhuman.projectcreate.game.utils.Inputs;

public class GameScreen implements Screen {

    private static volatile GameScreen instance;

    private GameScreen() {}

    @Override
    public void show() {
        GameManager.getInstance().init();
        Gdx.graphics.setResizable(true);
        Gdx.input.setInputProcessor(Inputs.getInstance());
    }

    @Override
    public void render(float delta) {
        GameManager.getInstance().update(delta);
        GameManager.getInstance().render();
    }

    @Override
    public void resize(int width, int height) {
        GameManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        GameManager.getInstance().delete();
    }

    public static GameScreen getInstance() {
        if(instance != null)
            return instance;
        synchronized (GameScreen.class) {
            if (instance == null)
                instance = new GameScreen();
        }
        return instance;
    }

}
