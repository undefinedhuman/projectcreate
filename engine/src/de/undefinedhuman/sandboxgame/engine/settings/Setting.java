package de.undefinedhuman.sandboxgame.engine.settings;

import de.undefinedhuman.sandboxgame.engine.file.FileWriter;

import java.util.HashMap;

public class Setting {

    private String key;
    private Object value;

    public Setting(String key, Object value) {
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
        return Boolean.parseBoolean(getString());
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

    public void load(HashMap<String, String> settings) {}
    public void save(FileWriter writer) {}

}
