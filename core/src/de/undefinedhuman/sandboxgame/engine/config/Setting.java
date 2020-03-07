package de.undefinedhuman.sandboxgame.engine.config;

public class Setting {

    private String name;
    private Object value;

    public Setting(String name, Object value) {
        this.name = name;
        this.value = value;
        SettingsManager.instance.getSettings().add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
