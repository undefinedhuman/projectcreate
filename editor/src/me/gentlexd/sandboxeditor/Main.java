package me.gentlexd.sandboxeditor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.gentlexd.sandboxeditor.editor.ItemEditor;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.engine.ressources.SoundManager;
import me.gentlexd.sandboxeditor.engine.ressources.TextureManager;
import me.gentlexd.sandboxeditor.engine.window.Window;

public class Main extends Game {

    public static Main instance;
    public static float delta;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    @Override
    public void create() {

        instance = this;
        load();

        camera = new OrthographicCamera();
        batch = new SpriteBatch();

    }

    @Override
    public void render() {

        clear();

        delta = Gdx.graphics.getDeltaTime();
        super.render();

    }

    @Override
    public void pause() {

        super.pause();

    }

    @Override
    public void dispose() {

        super.dispose();

        SoundManager.instance.delete();
        TextureManager.instance.delete();
        Log.instance.save();

    }

    @Override
    public void resize(int width, int height) {

        super.resize(width, height);

    }

    @Override
    public void resume() {

        super.resume();

    }

    private void load() {

        Log.instance = new Log();

        TextureManager.instance = new TextureManager();
        SoundManager.instance = new SoundManager();

    }

    private void clear() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

}
