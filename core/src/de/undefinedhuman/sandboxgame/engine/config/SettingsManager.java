package de.undefinedhuman.sandboxgame.engine.config;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;

import java.util.ArrayList;

public class SettingsManager extends Manager {

    public static SettingsManager instance;
    public Setting displayHeight, displayWidth, vSync, maxFps, fullScreen, language, renderHitbox, firstRun, guiScale;
    private final SettingsList settings;

    public SettingsManager() {
        if (instance == null) instance = this;
        this.settings = new SettingsList();
    }

    @Override
    public void init() {
        settings.addSettings(
                displayHeight = new Setting(SettingType.Int, "displayHeight", 720),
                displayWidth = new Setting(SettingType.Int, "displayWidth", 1280),
                vSync = new BooleanSetting("vSync", false),
                maxFps = new Setting(SettingType.Int, "maxFps", 0),
                fullScreen = new BooleanSetting("fullScreen", false),
                language = new Setting(SettingType.String, "language", "eu_DE"),
                firstRun = new BooleanSetting("firstRun", false),
                guiScale = new Setting(SettingType.Int, "guiScale", 5));
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

}
