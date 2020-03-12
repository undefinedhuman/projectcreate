package de.undefinedhuman.sandboxgame.engine.settings;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;

import java.util.HashMap;

public class Vector2Setting extends Setting {

    public Vector2Setting(String key, Object value) {
        super(SettingType.Vector2, key, value);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings) {
        value = settings.get(key).getNextVector2();
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeVector2(getVector2());
    }

}
