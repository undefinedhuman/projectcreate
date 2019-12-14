package de.undefinedhuman.sandboxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import de.undefinedhuman.sandboxgame.engine.config.ConfigManager;
import de.undefinedhuman.sandboxgame.engine.config.SettingsManager;
import de.undefinedhuman.sandboxgame.engine.language.LanguageManager;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.ressources.SoundManager;
import de.undefinedhuman.sandboxgame.engine.ressources.font.FontManager;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.window.Window;
import de.undefinedhuman.sandboxgame.screen.TestScreen;
import de.undefinedhuman.sandboxgame.utils.ManagerList;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class Main extends Game {

    public static Main instance;

    public static float delta;
    public static int guiScale = 1;

    private ManagerList managerList;

    public Main() {
        instance = this;
        managerList = new ManagerList();
        managerList.addManager(new Log(), new SettingsManager(), new ConfigManager(), new LanguageManager(), new TextureManager(), new SoundManager(), new FontManager());
    }

    @Override
    public void create() {
        managerList.init();
        initScreens();
        setScreen(TestScreen.instance);
    }

    @Override
    public void render() {
        delta = Gdx.graphics.getDeltaTime() * Variables.deltaMultiplier;
        managerList.update(delta);
        super.render();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
        managerList.delete();
        Window.instance.delete();
    }

    @Override
    public void resize(int width, int height) {
        int guiSetting = SettingsManager.instance.guiScale.getInt();
        guiScale = guiSetting == 5 ? Math.max(width / 640, 1) : guiSetting;
        managerList.resize(width, height);
        super.resize(width, height);
    }

    @Override
    public void resume() {
        super.resume();
    }

    private void initScreens() {
        TestScreen.instance = new TestScreen();
    }

}
