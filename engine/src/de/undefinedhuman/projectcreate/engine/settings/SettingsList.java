package de.undefinedhuman.projectcreate.engine.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsList {

    protected ArrayList<Setting<?>> settings = new ArrayList<>();

    public void addSettings(Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public void delete() {
        for(Setting<?> setting : settings)
            setting.delete();
        settings.clear();
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public boolean isEmpty() {
        return settings.size() == 0;
    }

}
