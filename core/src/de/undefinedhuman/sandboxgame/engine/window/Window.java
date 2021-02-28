package de.undefinedhuman.sandboxgame.engine.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

public class Window {

    public static Window instance;

    public Window() {
        new LwjglApplication(new Main(), createLWJGLConfig());
    }

    private LwjglApplicationConfiguration createLWJGLConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SandboxGame";
        config.foregroundFPS = 400;
        config.backgroundFPS = 60;
        config.vSyncEnabled = false;
        config.width = Variables.BASE_WINDOW_WIDTH;
        config.height = Variables.BASE_WINDOW_HEIGHT;
        return config;
    }

    public void update() {
        Gdx.graphics.setTitle("SandboxGame FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    public void delete() {
        Gdx.app.exit();
    }

}
