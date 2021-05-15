package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class ByteSetting extends NumberSetting<Byte> {
    public ByteSetting(String key, Byte defaultValue) {
        super(key, defaultValue, Tools::isByte, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public ByteSetting(String key, Byte defaultValue, Byte min, Byte max) {
        super(key, defaultValue, Tools::isByte, min, max);
    }
}
