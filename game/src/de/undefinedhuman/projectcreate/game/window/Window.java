package de.undefinedhuman.projectcreate.game.window;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.Main;

public class Window {

    private static volatile Window instance;
    private boolean running = false;

    public Window() {}

    public void createNewApplication() {
        if(running) return;
        new Lwjgl3Application(Main.getInstance(), createLWJGLConfig());
        running = true;
    }

    private Lwjgl3ApplicationConfiguration createLWJGLConfig() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(Variables.NAME);
        config.setForegroundFPS(0);
        config.setIdleFPS(60);
        config.setMaxNetThreads(0);
        config.useVsync(false);
        config.setWindowIcon(Files.FileType.Internal, "logo/32x32.png", "logo/64x64.png");
        config.setWindowSizeLimits(Variables.BASE_WINDOW_WIDTH, Variables.BASE_WINDOW_HEIGHT, -1, -1);
        return config;
    }

    public void update() {
        Gdx.graphics.setTitle(Variables.NAME + ", " + Variables.VERSION + " FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    public void delete() {
        Gdx.app.exit();
    }

    public static Window getInstance() {
        if(instance != null)
            return instance;
        synchronized (Window.class) {
            if (instance == null)
                instance = new Window();
        }
        return instance;
    }

}
