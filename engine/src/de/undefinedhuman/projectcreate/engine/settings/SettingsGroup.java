package de.undefinedhuman.projectcreate.engine.settings;

public class SettingsGroup extends SettingsList {

    private String title;

    public SettingsGroup(String title, Setting<?>... settings) {
        this.title = title;
        addSettings(settings);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void delete() {
        getSettings().clear();
    }
}
