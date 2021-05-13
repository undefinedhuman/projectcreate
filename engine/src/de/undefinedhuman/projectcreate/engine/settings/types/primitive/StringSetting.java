package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class StringSetting extends Setting<String> {
    public StringSetting(String key, Object defaultValue) {
        super(key, defaultValue, String::valueOf);
    }
}
