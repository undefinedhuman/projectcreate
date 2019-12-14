package de.undefinedhuman.sandboxgame.engine.config;

import java.util.ArrayList;

public class Setting {

    private String name;
    private Object value;

    public Setting(String name, Object value, ArrayList<Setting> settings) {

        this.name = name;
        this.value = value;

        settings.add(this);

    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(getString());
    }

    public float getFloat() {
        return Float.parseFloat(getString());
    }

    public int getInt() {
        return Integer.parseInt(getString());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
