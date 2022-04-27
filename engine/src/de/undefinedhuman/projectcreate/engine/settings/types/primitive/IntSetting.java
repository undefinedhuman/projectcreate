package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class IntSetting extends NumberSetting<Integer> {
    public IntSetting(String key, Integer defaultValue) {
        super(key, defaultValue, Utils::isInteger);
    }

    public IntSetting(String key, Integer defaultValue, Integer min, Integer max) {
        super(key, defaultValue, Utils::isInteger, min, max);
    }
}
