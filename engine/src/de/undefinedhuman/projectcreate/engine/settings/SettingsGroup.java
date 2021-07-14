package de.undefinedhuman.projectcreate.engine.settings;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsGroup {

    private String title;
    private ArrayList<Setting<?>> settings = new ArrayList<>();

    public SettingsGroup(String title, Setting<?>... settings) {
        this.title = title;
        addSettings(settings);
    }

    public void addSettings(Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public ArrayList<Setting<?>> getSettings() {
        return settings;
    }

    public String getTitle() {
        return title;
    }

    public void delete() {
        settings.clear();
    }

}
