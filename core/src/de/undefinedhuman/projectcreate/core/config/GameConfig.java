package de.undefinedhuman.projectcreate.core.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.types.BooleanSetting;

public class GameConfig extends Config {

    public static GameConfig instance;

    public Setting
        displayWidth = new Setting(SettingType.Int, "displayWidth", 1280),
        displayHeight = new Setting(SettingType.Int, "displayHeight", 720),
        vSync = new BooleanSetting("vSync", false),
        maxFps = new Setting(SettingType.Int, "maxFPS", 0),
        fullScreen = new BooleanSetting("fullScreen", false),
        language = new Setting(SettingType.String, "language", "eu_DE"),
        renderHitboxes = new BooleanSetting("renderHitboxes", false),
        firstRun = new BooleanSetting("firstRun", false),
        guiScale = new Setting(SettingType.Int, "guiScale", 5);


    public GameConfig() {
        super("game");
        if(instance == null)
            instance = this;
        addSettings(displayWidth, displayHeight, vSync, maxFps, fullScreen, language, renderHitboxes, firstRun, guiScale);
    }

}