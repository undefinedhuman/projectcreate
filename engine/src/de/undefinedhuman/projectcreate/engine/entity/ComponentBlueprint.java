package de.undefinedhuman.projectcreate.engine.entity;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public abstract class ComponentBlueprint {

    protected ComponentType type;
    protected SettingsList settings = new SettingsList();

    public ComponentBlueprint() {}

    public ComponentType getType() {
        return type;
    }

    public abstract Component createInstance(HashMap<ComponentType, ComponentParam> params);

    public void load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting setting : this.settings.getSettings())
            setting.loadSetting(parentDir, settingsObject);
    }

    public void save(FileWriter writer) {
        writer.writeString("{:" + type.name()).nextLine();
        Tools.saveSettings(writer, settings);
        writer.writeString("}").nextLine();
    }

    public SettingsList getSettingsList() {
        return settings;
    }

    public List<Setting> getSettings() {
        return settings.getSettings();
    }

    public Stream<Setting> getSettingsStream() {
        return settings.getSettings().stream();
    }

    public void delete() {
        settings.delete();
    }

}
