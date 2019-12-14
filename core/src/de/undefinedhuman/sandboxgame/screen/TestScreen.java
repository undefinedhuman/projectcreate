package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestScreen implements Screen {

    public static TestScreen instance;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();

        Gdx.graphics.setResizable(true);

    }

    @Override
    public void render(float delta) {

        clear();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

        camera.setToOrtho(false, width, height);
        viewport.update(width, height);
        camera.update();

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    private void clear() {

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

    }

}
