package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.settings.types.TextFieldSetting;

public class StringSetting extends TextFieldSetting<String> {
    public StringSetting(String key, String defaultValue) {
        super(key, defaultValue, value -> value, value -> value);
    }
}
