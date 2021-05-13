package de.undefinedhuman.projectcreate.engine.ecs;

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

    private Class<? extends Component> type;

    protected SettingsList settings = new SettingsList();

    public ComponentBlueprint(Class<? extends Component> type) {
        this.type = type;
    }

    public Class<? extends Component> getType() {
        return type;
    }

    public abstract Component createInstance(HashMap<Class<? extends Component>, ComponentParam> params);

    public void load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting<?> setting : this.settings.getSettings())
            setting.loadSetting(parentDir, settingsObject);
    }

    public void save(FileWriter writer) {
        writer.writeString("{:" + type.getName()).nextLine();
        Tools.saveSettings(writer, settings);
        writer.writeString("}").nextLine();
    }

    public SettingsList getSettingsList() {
        return settings;
    }

    public List<Setting<?>> getSettings() {
        return settings.getSettings();
    }

    public Stream<Setting<?>> getSettingsStream() {
        return settings.getSettings().stream();
    }

    public void delete() {
        settings.delete();
    }

}
