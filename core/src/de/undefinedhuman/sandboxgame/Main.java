package de.undefinedhuman.sandboxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.undefinedhuman.sandboxgame.engine.config.ConfigManager;
import de.undefinedhuman.sandboxgame.engine.config.SettingsManager;
import de.undefinedhuman.sandboxgame.engine.language.LanguageManager;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.resources.SoundManager;
import de.undefinedhuman.sandboxgame.engine.resources.font.FontManager;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.ManagerList;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.window.Window;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgame.equip.EquipManager;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.network.ClientManager;
import de.undefinedhuman.sandboxgame.screen.TestScreen;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameScreen;
import de.undefinedhuman.sandboxgame.utils.Inputs;
import de.undefinedhuman.sandboxgame.utils.Timer;
import de.undefinedhuman.sandboxgame.world.WorldManager;
import de.undefinedhuman.sandboxgame.world.layer.topLayer.TopLayerManager;

public class Main extends Game {

    public static Main instance;

    public static float delta;
    public static int guiScale = 1;

    private ManagerList managerList;
    private Timer timer;

    public Main() {
        instance = this;
        managerList = new ManagerList();
        managerList.addManager(new Log(), new SettingsManager(), new ConfigManager(), new LanguageManager(), new TextureManager(), new SoundManager(), new FontManager(), new Inputs(), new GuiManager(), new BlueprintManager(), new ItemManager());
        timer = new Timer(1, true, () -> Window.instance.update());
    }

    @Override
    public void create() {
        managerList.init();
        initScreens();
        setScreen(TestScreen.instance);
    }

    @Override
    public void resize(int width, int height) {
        int guiSetting = SettingsManager.instance.guiScale.getInt();
        guiScale = Math.max(guiSetting == 5 ? width/640 : guiSetting, 1);
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    private void initScreens() {

        EntityManager.instance = new EntityManager();

        TestScreen.instance = new TestScreen();
        GameScreen.instance = new GameScreen();

        EquipManager.instance = new EquipManager();
        InventoryManager.instance = new InventoryManager();

        WorldManager.instance = new WorldManager();

        ClientManager.instance = new ClientManager();

        TopLayerManager.instance = new TopLayerManager();
        TopLayerManager.instance.load();
    }

}
