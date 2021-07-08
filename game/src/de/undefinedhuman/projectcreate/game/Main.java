package de.undefinedhuman.projectcreate.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.language.LanguageManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.resources.sound.SoundManager;
import de.undefinedhuman.projectcreate.engine.resources.font.FontManager;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.config.GameConfig;
import de.undefinedhuman.projectcreate.game.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.game.equip.EquipManager;
import de.undefinedhuman.projectcreate.game.gui.GuiManager;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTextureManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.screen.TestScreen;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.game.utils.Inputs;
import de.undefinedhuman.projectcreate.game.utils.Timer;
import de.undefinedhuman.projectcreate.game.window.Window;
import de.undefinedhuman.projectcreate.game.world.WorldManager;
import de.undefinedhuman.projectcreate.game.world.layer.topLayer.TopLayerManager;

public class Main extends Game {

    public static Main instance;

    public static float delta;
    public static int guiScale = 1;

    private ManagerList managerList;
    private Timer timer;

    public Main() {
        instance = this;
        managerList = new ManagerList();
        managerList.addManager(
                Log.getInstance().setLogMessageDecorator(
                    new LogMessage().andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT)).andThen(value -> LogMessageDecorators.withModuleName(value, "Game"))
                ),
                ConfigManager.getInstance().setConfigs(GameConfig.getInstance()),
                LanguageManager.getInstance(),
                TextureManager.getInstance(),
                SoundManager.getInstance(),
                FontManager.getInstance(),
                Inputs.getInstance(),
                GuiManager.getInstance(),
                GuiTextureManager.getInstance(),
                BlueprintManager.getInstance(),
                ItemManager.getInstance());
        timer = new Timer(1, true, () -> Window.instance.update());
    }

    @Override
    public void create() {
        initGDX();
        managerList.init();
        initScreens();
        setScreen(TestScreen.instance);
    }

    @Override
    public void resize(int width, int height) {
        int guiSetting = GameConfig.getInstance().guiScale.getValue();
        guiScale = (int) Math.max((guiSetting == 5 ? Math.ceil(width/640f) : guiSetting) / 2, 1);
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
        delta = Gdx.graphics.getDeltaTime() * Variables.DELTA_MULTIPLIER;
        managerList.update(delta);
        timer.update(delta);
        clear();
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        managerList.delete();
        Window.instance.delete();
    }

    private void clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }

    private void initGDX() {
        Gdx.app.setApplicationLogger(Log.getInstance());
        Gdx.app.setLogLevel(Variables.LOG_LEVEL.ordinal());
        Pixmap cursor = new Pixmap(Gdx.files.internal(Paths.GUI_PATH + "Cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
        cursor.dispose();
    }

    private void initScreens() {
        EntityManager.getInstance();

        TestScreen.instance = new TestScreen();
        GameScreen.instance = new GameScreen();

        EquipManager.getInstance();
        InventoryManager.getInstance();

        WorldManager.getInstance();

        ClientManager.getInstance();

        TopLayerManager.instance = new TopLayerManager();
        TopLayerManager.instance.load();
    }

}
