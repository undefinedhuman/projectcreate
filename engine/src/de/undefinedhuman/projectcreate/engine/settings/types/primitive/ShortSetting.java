package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class ShortSetting extends NumberSetting<Short> {
    public ShortSetting(String key, Short defaultValue) {
        super(key, defaultValue, Utils::isShort);
    }

    public ShortSetting(String key, Short defaultValue, Short min, Short max) {
        super(key, defaultValue, Utils::isShort, min, max);
    }
}
