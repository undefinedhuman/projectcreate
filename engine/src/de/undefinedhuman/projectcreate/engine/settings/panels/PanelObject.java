package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class PanelObject extends SettingsList implements Comparable<PanelObject> {

    private String key;

    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        Tools.saveSettings(writer, this);
        writer.writeString("}").nextLine();
    }

    public PanelObject load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting<?> setting : settings)
            setting.load(parentDir, settingsObject);
        return this;
    }

    public PanelObject setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int compareTo(PanelObject o) {
        return getKey().compareTo(o.getKey());
    }
}
