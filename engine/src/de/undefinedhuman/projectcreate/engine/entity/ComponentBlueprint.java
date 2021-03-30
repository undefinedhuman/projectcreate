package de.undefinedhuman.projectcreate.engine.entity;

import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ComponentBlueprint {

    protected ComponentType type;
    protected SettingsList settings = new SettingsList();

    public ComponentBlueprint() {}

    public ComponentType getType() {
        return type;
    }

    public abstract Component createInstance(HashMap<ComponentType, ComponentParam> params);

    public void load(FsFile parentDir, SettingsObject settingsObject) {
        for(Setting setting : this.settings.getSettings())
            setting.loadSetting(parentDir, settingsObject);
    }

    public void save(FileWriter writer) {
        writer.writeString("{:" + type.name()).nextLine();
        Tools.saveSettings(writer, settings.getSettings());
        writer.writeString("}").nextLine();
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

    public void delete() {
        settings.delete();
    }

}
