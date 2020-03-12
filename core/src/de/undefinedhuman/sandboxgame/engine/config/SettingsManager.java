package de.undefinedhuman.sandboxgame.engine.config;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;

import java.util.ArrayList;

public class SettingsManager extends Manager {

    public static SettingsManager instance;
    public Setting displayHeight, displayWidth, fullScreen, language, renderHitbox, firstRun, RGB, fullBright, dayLight, guiScale;
    private SettingsList settings;

    public SettingsManager() {
        if (instance == null) instance = this;
        this.settings = new SettingsList();
    }

    @Override
    public void init() {
        settings.addSettings(
                displayHeight = new Setting("displayHeight", 720),
                displayWidth = new Setting("displayWidth", 1280),
                fullScreen = new Setting("fullScreen", false),
                language = new Setting("language", "eu_DE"),
                renderHitbox = new Setting("renderHitBox", false),
                firstRun = new Setting("firstRun", false),
                RGB = new Setting("RGB", true),
                fullBright = new Setting("fullBright", false),
                dayLight = new Setting("dayLight", 1.0f),
                guiScale = new Setting("guiScale", 5));
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

}
