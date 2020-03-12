package de.undefinedhuman.sandboxgame.engine.settings;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.Rarity;

import java.util.HashMap;

public class RaritySetting extends Setting {

    public RaritySetting(SettingType type, String key, Object value) {
        super(SettingType.Rarity, key, value);
    }

    @Override
    public void load(HashMap<String, LineSplitter> settings) {
        value = Rarity.valueOf(settings.get(key).getNextString());
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeValue(getRarity().name());
    }

}
