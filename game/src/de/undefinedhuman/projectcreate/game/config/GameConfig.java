package de.undefinedhuman.projectcreate.game.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class GameConfig extends Config {

    private static volatile GameConfig instance;

    public IntSetting
            displayWidth = new IntSetting("displayWidth", 1280),
            displayHeight = new IntSetting("displayHeight", 720),
            maxFps = new IntSetting("maxFPS", 0),
            guiScale = new IntSetting("guiScale", 5);
    public BooleanSetting
            vSync = new BooleanSetting("vSync", false),
            fullScreen = new BooleanSetting("fullScreen", false),
            renderHitboxes = new BooleanSetting("renderHitboxes", false),
            firstRun = new BooleanSetting("firstRun", false);
    public StringSetting
            language = new StringSetting("language", "eu_DE");

    private GameConfig() {
        super("game", true);
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
