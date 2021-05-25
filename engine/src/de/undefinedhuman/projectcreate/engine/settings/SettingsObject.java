package de.undefinedhuman.projectcreate.engine.settings;

import java.util.HashMap;

public class SettingsObject {

    private HashMap<String, Object> settings = new HashMap<>();

    public void addSetting(String key, Object value) {
        this.settings.put(key, value);
    }

    public boolean containsKey(String key) {
        return settings.containsKey(key);
    }

    public Object get(String key) {
        return settings.get(key);
    }

    public HashMap<String, Object> getSettings() {
        return settings;
    }

    public void delete() {
        for(Object value : settings.values()) {
            if(!(value instanceof SettingsObject))
                continue;
            ((SettingsObject) value).delete();
        }
        settings.clear();
    }

}
