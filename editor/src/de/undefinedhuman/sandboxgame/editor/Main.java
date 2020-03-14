package de.undefinedhuman.sandboxgame.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.undefinedhuman.sandboxgame.editor.engine.EditorScreen;
import de.undefinedhuman.sandboxgame.editor.engine.ressources.SoundManager;
import de.undefinedhuman.sandboxgame.editor.engine.ressources.TextureManager;
import de.undefinedhuman.sandboxgame.editor.engine.window.Window;
import de.undefinedhuman.sandboxgame.engine.log.Log;

import java.awt.*;

public class Main extends Game {

    public static Main instance;
    public static float delta;

    @Override
    public void create() {
        instance = this;
        load();
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
        EditorScreen.instance = new EditorScreen();
        setScreen(EditorScreen.instance);
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> Window.instance = new Window());
    }

}
