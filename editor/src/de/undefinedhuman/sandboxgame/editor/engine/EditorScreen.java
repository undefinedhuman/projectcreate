package de.undefinedhuman.sandboxgame.editor.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.gentlexd.sandboxeditor.engine.ressources.TextureManager;

public class EditorScreen implements Screen {

    public static EditorScreen instance;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public EditorScreen() {

        camera = new OrthographicCamera();
        batch = new SpriteBatch();

    }


    @Override
    public void show() {}

    @Override
    public void render(float delta) {

        updateCamera();

        batch.setColor(Color.WHITE);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = 480;
        camera.viewportHeight = ((float) height / (float) width) * 480;
        camera.update();

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

        TextureManager.instance.delete();

    }

    private void updateCamera() {

        camera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        camera.update();

    }

}
