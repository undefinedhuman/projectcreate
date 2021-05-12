package de.undefinedhuman.projectcreate.game.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.BooleanSetting;

public class GameConfig extends Config {

    private static volatile GameConfig instance;

    public Setting
        displayWidth = new Setting("displayWidth", 1280),
        displayHeight = new Setting("displayHeight", 720),
        vSync = new BooleanSetting("vSync", false),
        maxFps = new Setting("maxFPS", 0),
        fullScreen = new BooleanSetting("fullScreen", false),
        language = new Setting("language", "eu_DE"),
        renderHitboxes = new BooleanSetting("renderHitboxes", false),
        firstRun = new BooleanSetting("firstRun", false),
        guiScale = new Setting("guiScale", 5);


    private GameConfig() {
        super("game");
        addSettings(displayWidth, displayHeight, vSync, maxFps, fullScreen, language, renderHitboxes, firstRun, guiScale);
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            synchronized (GameConfig.class) {
                if (instance == null)
                    instance = new GameConfig();
            }
        }
        return instance;
    }

    @Override
    public void validate() {}
}
