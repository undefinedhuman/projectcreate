package de.undefinedhuman.sandboxgame.engine.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.undefinedhuman.sandboxgame.Main;

public class Window {

    public static Window instance;

    public Window() {
        new LwjglApplication(new Main(), createLWJGLConfig());
    }

    private LwjglApplicationConfiguration createLWJGLConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SandboxGame";
        config.vSyncEnabled = false;
        config.foregroundFPS = 120;
        config.backgroundFPS = 120;
        config.vSyncEnabled = true;
        config.width = 1280;
        config.height = 720;
        return config;
    }

    public void update() {
        Gdx.graphics.setTitle("SandboxGame FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    public void delete() {
        Gdx.app.exit();
    }

}
