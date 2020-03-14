package de.undefinedhuman.sandboxgame.engine.settings;

import de.undefinedhuman.sandboxgame.engine.utils.MultiMap;

import java.util.ArrayList;

public class SettingsList {

    private MultiMap<SettingType, Setting> settings = new MultiMap<>();

    public void addSettings(Setting... settings) {
        for(Setting setting : settings) this.settings.add(setting.getType(), setting);
    }

    public void delete() {
        settings.clear();
    }

    public ArrayList<Setting> getSettings() {
        return settings.getAllValues();
    }

}
