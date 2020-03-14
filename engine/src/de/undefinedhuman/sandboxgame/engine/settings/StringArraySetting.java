package de.undefinedhuman.sandboxgame.engine.settings;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;

import java.util.HashMap;

public class StringArraySetting extends Setting {

    public StringArraySetting(String key, String[] value) {
        super(SettingType.StringArray, key, value);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings) {
        LineSplitter splitter = settings.get(key);
        String[] strings = new String[splitter.getNextInt()];
        for(int i = 0; i < strings.length; i++) strings[i] = splitter.getNextString();
        value = strings;
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeInt(getStringArray().length);
        for(String s : getStringArray()) writer.writeString(s);
    }

}
