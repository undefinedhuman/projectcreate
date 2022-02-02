package de.undefinedhuman.projectcreate.engine.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingsList {

    private ArrayList<Setting<?>> settings = new ArrayList<>();

    public void addSettings(Setting<?>... settings) {
        for(Setting setting : settings)
            addSetting(setting);
    }

    public void removeSettings(Setting<?>... settings) {
        for(Setting setting : settings)
            removeSetting(setting);
    }

    public void delete() {
        removeSettings(settings.toArray(new Setting[0]));
    }

    protected void addSetting(Setting<?> setting) {
        if(hasSetting(setting))
            return;
        this.settings.add(setting);
    }

    protected void removeSetting(Setting<?> setting) {
        if(!hasSetting(setting))
            return;
        this.settings.remove(setting);
        setting.delete();
    }

    public boolean hasSetting(Setting<?> setting) {
        return settings.contains(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public boolean isEmpty() {
        return settings.size() == 0;
    }

}
