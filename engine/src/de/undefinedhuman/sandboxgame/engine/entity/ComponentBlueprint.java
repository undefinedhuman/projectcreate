package de.undefinedhuman.sandboxgame.engine.entity;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
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

    public void load(FileReader reader) {
        HashMap<String, LineSplitter> settings = Tools.loadSettings(reader);
        for(Setting setting : this.settings.getSettings()) setting.loadSetting(reader.getParentDirectory(), settings);
    }

    public void save(FileWriter writer) {
        Tools.saveSetting(writer, settings.getSettings());
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

    public void delete() {
        settings.delete();
    }

}
