package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import java.util.ArrayList;

public class PanelObject {

    public String key;

    protected SettingsList settings = new SettingsList();

    public void save(FileWriter writer) {
        writer.writeString("{:" + getKey()).nextLine();
        Tools.saveSettings(writer, settings);
        writer.writeString("}").nextLine();
    }

    public PanelObject load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting setting : settings.getSettings())
            setting.loadSetting(parentDir, settingsObject);
        return this;
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

    public PanelObject setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKey() {
        return key;
    }

    public void delete() {
        settings.delete();
    }

}
