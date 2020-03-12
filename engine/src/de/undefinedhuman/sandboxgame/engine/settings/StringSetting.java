package de.undefinedhuman.sandboxgame.engine.settings;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;

import java.util.HashMap;

public class StringSetting extends Setting {

    public StringSetting(String key, Object value) {
        super(key, value);
    }

    @Override
    public void save(FileWriter writer) {
        super.save(writer);
        writer.writeValue(getValue());
    }

    @Override
    public void load(HashMap<String, String> settings) {
        super.load(settings);
        if(settings.containsKey(getKey())) setValue(settings.get(getKey()));
    }

}
