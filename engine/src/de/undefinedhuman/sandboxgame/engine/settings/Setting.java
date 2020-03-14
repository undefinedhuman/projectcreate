package de.undefinedhuman.sandboxgame.engine.settings;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileUtils;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.Rarity;

import java.util.HashMap;

public class Setting {

    private SettingType type;

    protected String key;
    protected Object value;

    public Setting(String key, Object value) {
        this(SettingType.String, key, value);
    }

    public Setting(SettingType type, String key, Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getBoolean() {
        return FileUtils.readBoolean(getString());
    }

    public String getString() {
        return String.valueOf(value);
    }

    public float getFloat() {
        return Float.parseFloat(getString());
    }

    public int getInt() {
        return Integer.parseInt(getString());
    }

    public Rarity getRarity() {
        return (Rarity) value;
    }

    public String[] getStringArray() {
        return (String[]) value;
    }

    public Vector2 getVector2() {
        return (Vector2) value;
    }

    public SettingType getType() {
        return type;
    }

    public void load(HashMap<String, LineSplitter> settings) {
        this.value = settings.get(key).getNextString();
    }

    public void save(FileWriter writer) {
        writer.writeValue(key).writeValue(value.toString());
    }

}
