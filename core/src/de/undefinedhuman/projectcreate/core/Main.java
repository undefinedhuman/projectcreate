package de.undefinedhuman.projectcreate.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import de.undefinedhuman.projectcreate.core.config.GameConfig;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.language.LanguageManager;
import de.undefinedhuman.projectcreate.core.window.Window;
import de.undefinedhuman.projectcreate.core.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.core.world.layer.topLayer.TopLayerManager;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.SoundManager;
import de.undefinedhuman.projectcreate.engine.resources.font.FontManager;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.core.entity.EntityManager;
import de.undefinedhuman.projectcreate.core.equip.EquipManager;
import de.undefinedhuman.projectcreate.core.gui.GuiManager;
import de.undefinedhuman.projectcreate.core.gui.texture.GuiTextureManager;
import de.undefinedhuman.projectcreate.core.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.core.item.ItemManager;
import de.undefinedhuman.projectcreate.core.network.ClientManager;
import de.undefinedhuman.projectcreate.core.screen.TestScreen;
import de.undefinedhuman.projectcreate.core.utils.Inputs;
import de.undefinedhuman.projectcreate.core.utils.Timer;
import de.undefinedhuman.projectcreate.core.world.WorldManager;

public class Main extends Game {

    public static Main instance;

    public static float delta;
    public static int guiScale = 1;

    private ManagerList managerList;
    private Timer timer;

    public Main() {
        // TEST DEV COMMIT

        instance = this;
        managerList = new ManagerList();
        managerList.addManager(new Log(), ConfigManager.getInstance().setConfigs(GameConfig.getInstance()), new LanguageManager(), new TextureManager(), new SoundManager(), new FontManager(), new Inputs(), new GuiManager(), new GuiTextureManager(), BlueprintManager.getInstance(), ItemManager.getInstance());
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
        int guiSetting = GameConfig.getInstance().guiScale.getInt();
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
        Gdx.app.setApplicationLogger(Log.instance);
        Gdx.app.setLogLevel(Variables.LOG_LEVEL);
        Pixmap cursor = new Pixmap(Gdx.files.internal(Paths.GUI_PATH + "Cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
        cursor.dispose();
    }

    private void initScreens() {
        EntityManager.getInstance();

        TestScreen.instance = new TestScreen();
        GameScreen.instance = new GameScreen();

        EquipManager.getInstance();
        InventoryManager.instance = new InventoryManager();

        WorldManager.instance = new WorldManager();

        ClientManager.instance = new ClientManager();

        TopLayerManager.instance = new TopLayerManager();
        TopLayerManager.instance.load();
    }

}
