package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class IntSetting extends Setting<Integer> {

    public IntSetting(String key, Object defaultValue) {
        super(key, defaultValue, value -> Integer.parseInt(String.valueOf(value)));
    }

}
