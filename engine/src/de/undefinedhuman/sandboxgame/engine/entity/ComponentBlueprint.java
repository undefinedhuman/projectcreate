package de.undefinedhuman.sandboxgame.engine.entity;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsObject;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

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
