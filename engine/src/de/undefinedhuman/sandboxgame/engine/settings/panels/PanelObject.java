package de.undefinedhuman.sandboxgame.engine.settings.panels;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class PanelObject {

    protected SettingsList settings = new SettingsList();

    public PanelObject() {}

    public void save(FileWriter writer) {
        Tools.saveSetting(writer, settings.getSettings());
    }

    public PanelObject load(FileReader reader) {
        HashMap<String, LineSplitter> settings = Tools.loadSettings(reader);
        for(Setting setting : this.settings.getSettings()) setting.loadSetting(reader.getParentDirectory(), settings);
        return this;
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

    public void delete() {
        settings.delete();
    }

}
