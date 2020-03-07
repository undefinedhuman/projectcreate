package de.undefinedhuman.sandboxgame.engine.config;

import de.undefinedhuman.sandboxgame.utils.Manager;

import java.util.ArrayList;

public class SettingsManager extends Manager {

    public static SettingsManager instance;
    public Setting displayHeight, displayWidth, fullScreen, language, renderHitbox, firstRun, RGB, fullBright, dayLight, guiScale;
    ArrayList<Setting> settings;

    public SettingsManager() {
        if (instance == null) instance = this;
        this.settings = new ArrayList<>();
    }

    @Override
    public void init() {
        this.displayHeight = new Setting("displayHeight", 720);
        this.displayWidth = new Setting("displayWidth", 1280);
        this.fullScreen = new Setting("fullScreen", false);
        this.language = new Setting("language", "eu_DE");
        this.renderHitbox = new Setting("renderHitBox", false);
        this.firstRun = new Setting("firstRun", false);
        this.RGB = new Setting("RGB", true);
        this.fullBright = new Setting("fullBright", false);
        this.dayLight = new Setting("dayLight", 1.0f);
        this.guiScale = new Setting("guiScale", 5);
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

}
