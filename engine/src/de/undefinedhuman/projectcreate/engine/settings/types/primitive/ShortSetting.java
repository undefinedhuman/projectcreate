package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class ShortSetting extends NumberSetting<Short> {
    public ShortSetting(String key, Short defaultValue) {
        super(key, defaultValue, Tools::isShort, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    public ShortSetting(String key, Short defaultValue, Short min, Short max) {
        super(key, defaultValue, Tools::isShort, min, max);
    }
}
