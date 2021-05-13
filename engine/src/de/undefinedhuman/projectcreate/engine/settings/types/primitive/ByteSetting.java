package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class ByteSetting extends Setting<Byte> {

    public ByteSetting(String key, Object defaultValue) {
        super(key, defaultValue, value -> Byte.parseByte(String.valueOf(value)));
    }

}
