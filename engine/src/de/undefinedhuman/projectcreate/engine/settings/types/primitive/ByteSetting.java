package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class ByteSetting extends NumberSetting<Byte> {
    public ByteSetting(String key, Byte defaultValue) {
        super(key, defaultValue, Utils::isByte);
    }

    public ByteSetting(String key, Byte defaultValue, Byte min, Byte max) {
        super(key, defaultValue, Utils::isByte, min, max);
    }
}
