package de.undefinedhuman.sandboxgame.engine.config;

import de.undefinedhuman.sandboxgame.utils.Manager;

import java.util.ArrayList;

public class SettingsManager extends Manager {

    public static SettingsManager instance;

    ArrayList<Setting> settings;
    public Setting displayHeight, displayWidth, fullScreen, language, renderHitbox, firstRun, RGB, fullBright, dayLight, guiScale;

    public SettingsManager() {
        this.settings = new ArrayList<>();
        if(instance == null) instance = this;
    }

    @Override
    public void init() {
        this.displayHeight = new Setting("displayHeight",720, settings);
        this.displayWidth = new Setting("displayWidth",1280, settings);
        this.fullScreen = new Setting("fullScreen",false, settings);
        this.language = new Setting("language","eu_DE", settings);
        this.renderHitbox = new Setting("renderHitBox", false, settings);
        this.firstRun = new Setting("firstRun",false, settings);
        this.RGB = new Setting("RGB",true, settings);
        this.fullBright = new Setting("fullBright",false, settings);
        this.dayLight = new Setting("dayLight",1.0f, settings);
        this.guiScale = new Setting("guiScale",5, settings);
    }

}
