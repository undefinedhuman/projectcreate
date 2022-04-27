package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class PanelObject<K extends Comparable<K>> extends SettingsList implements Comparable<PanelObject<K>> {

    private K key;

    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        Utils.saveSettings(writer, this);
        writer.writeString("}").nextLine();
    }

    public PanelObject<K> load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting<?> setting : getSettings())
            setting.load(parentDir, settingsObject);
        return this;
    }

    public PanelObject<K> setKey(K key) {
        this.key = key;
        return this;
    }

    public K getKey() {
        return key;
    }

    public String getDisplayText() {
        return key.toString();
    }

    @Override
    public int compareTo(PanelObject<K> o) {
        return getKey().compareTo(o.getKey());
    }
}
