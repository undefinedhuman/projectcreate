package de.undefinedhuman.projectcreate.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import de.undefinedhuman.projectcreate.core.ecs.ComponentTypes;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.log.NetworkLogger;
import de.undefinedhuman.projectcreate.core.utils.ApplicationLoggerAdapter;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.texture.GuiTextureManager;
import de.undefinedhuman.projectcreate.engine.language.LanguageManager;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.resources.font.FontManager;
import de.undefinedhuman.projectcreate.engine.resources.sound.SoundManager;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.manager.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.timer.Timer;
import de.undefinedhuman.projectcreate.game.config.GameConfig;
import de.undefinedhuman.projectcreate.game.entity.system.*;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.TestScreen;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.game.utils.Inputs;
import de.undefinedhuman.projectcreate.game.window.Window;

public class Main extends Game {

    private static volatile Main instance;

    public static float delta;

    private ManagerList managerList;
    private Timer timer;

    private Main() {
        managerList = new ManagerList();
        managerList.addManager(
                Log.getInstance().setLogMessageDecorator(
                    new LogMessage()
                            .andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT))
                            .andThen(value -> LogMessageDecorators.withModuleName(value, "Game"))
                ).setLogLevel(Level.INFO),
                ConfigManager.getInstance().addConfigs(GameConfig.getInstance()),
                LanguageManager.getInstance(),
                TextureManager.getInstance(),
                SoundManager.getInstance(),
                FontManager.getInstance(),
                Inputs.getInstance(),
                GuiTextureManager.getInstance(),
                GuiManager.getInstance(),
                BlueprintManager.getInstance(),
                EntityManager.getInstance(),
                ItemManager.getInstance(),
                ClientManager.getInstance());
        timer = new Timer(1, () -> Window.getInstance().update());
    }

    @Override
    public void create() {
        initGDX();
        EntityManager.getInstance().addSystems(new MouseSystem(), new AnimationSystem(), new InteractionSystem(), new EquipSystem(), new MovementSystem(), new CameraSystem(), new RenderSystem());
        ComponentTypes.registerComponentTypes(BlueprintManager.getInstance());
        managerList.init();
        TEMP();
        setScreen(new TestScreen());
    }

    @Override
    public void resize(int width, int height) {
        managerList.resize(width, height);
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
        timer.update(delta);
        clear();
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        managerList.delete();
        Window.getInstance().delete();
    }

    private void clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }

    private void initGDX() {
        Gdx.app.setApplicationLogger(ApplicationLoggerAdapter.getInstance());
        Gdx.app.setLogLevel(Variables.LOG_LEVEL.ordinal());
        NetworkLogger.setLogger(Variables.LOG_LEVEL, Log::log);
        Pixmap cursor = new Pixmap(Gdx.files.internal(Paths.GUI_PATH + "Cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
        cursor.dispose();
    }

    private void TEMP() {
        GameScreen.getInstance();
        GameManager.getInstance();
    }

    public static Main getInstance() {
        if(instance != null)
            return instance;
        synchronized (Main.class) {
            if (instance == null)
                instance = new Main();
        }
        return instance;
    }

}
