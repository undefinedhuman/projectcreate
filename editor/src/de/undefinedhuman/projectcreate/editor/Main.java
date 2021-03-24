package de.undefinedhuman.projectcreate.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.awt.*;

public class Main extends Game {

    public static Main instance;
    public static float delta;

    @Override
    public void create() {
        if(instance == null) instance = this;
    }

    @Override
    public void render() {
        clear();
        delta = Gdx.graphics.getDeltaTime();
        Window.instance.updateErrorTime(delta);
        super.render();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void resume() {
        super.resume();
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> Window.instance = new Window());
    }

}
