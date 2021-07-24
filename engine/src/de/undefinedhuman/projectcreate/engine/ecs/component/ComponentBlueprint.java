package de.undefinedhuman.projectcreate.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import java.util.List;

public abstract class ComponentBlueprint {

    protected SettingsList settings = new SettingsList();

    public abstract Component createInstance();

    public void load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting<?> setting : this.settings.getSettings())
            setting.load(parentDir, settingsObject);
    }

    public void save(FileWriter writer) {
        writer.writeString("{:" + getClass().getName()).nextLine();
        Tools.saveSettings(writer, settings);
        writer.writeString("}").nextLine();
    }

    public List<Setting<?>> getSettings() {
        return settings.getSettings();
    }

    public void delete() {
        settings.delete();
    }
}
