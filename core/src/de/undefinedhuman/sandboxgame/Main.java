package de.undefinedhuman.sandboxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import de.undefinedhuman.sandboxgame.engine.config.ConfigManager;
import de.undefinedhuman.sandboxgame.engine.config.SettingsManager;
import de.undefinedhuman.sandboxgame.engine.language.LanguageManager;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.ressources.SoundManager;
import de.undefinedhuman.sandboxgame.engine.ressources.font.FontManager;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
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
import de.undefinedhuman.sandboxgame.world.World;
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

    /*
    Hair;Hair.png;3;16.0;1.0;
    Legs;Legs.png;2;16.0;1.0;
    RightArm;Front Arm.png;6;16.0;1.0;
    Head;Head.png;3;16.0;1.0;
    ItemHitbox;Unknown.png;0;0.0;0.0;
    SelectedArm;Front Arm S.png;6;16.0;1.0;
    Eye;Eyes.png;3;16.0;1.0;
    Item;Unknown.png;5;0.0;0.0;
    LeftArm;Back Arm.png;1;16.0;1.0;
    Body;Body.png;2;16.0;1.0;
     */

    @Override
    public void create() {
        managerList.init();
        initScreens();
        setScreen(TestScreen.instance);
    }

    private void initScreens() {

        TestScreen.instance = new TestScreen();
        GameScreen.instance = new GameScreen();

        EquipManager.instance = new EquipManager();
        InventoryManager.instance = new InventoryManager();

        EntityManager.instance = new EntityManager();

        World.instance = new World("Main", 50, 1000, 1000, 100);
        WorldManager.instance = new WorldManager();

        ClientManager.instance = new ClientManager();

        TopLayerManager.instance = new TopLayerManager();
        TopLayerManager.instance.load();
    }

    @Override
    public void dispose() {
        super.dispose();
        managerList.delete();
        Window.instance.delete();
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        delta = Gdx.graphics.getDeltaTime() * Variables.deltaMultiplier;
        managerList.update(delta);
        timer.update(delta);
        clear();
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        int guiSetting = SettingsManager.instance.guiScale.getInt();
        guiScale = guiSetting == 5 ? Math.max(width / 640, 1) : guiSetting;
        managerList.resize(width, height);
        super.resize(width, height);
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
    }

}
