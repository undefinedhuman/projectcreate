package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class IntSetting extends NumberSetting<Integer> {
    public IntSetting(String key, Integer defaultValue) {
        super(key, defaultValue, Tools::isInteger, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntSetting(String key, Integer defaultValue, Integer min, Integer max) {
        super(key, defaultValue, Tools::isInteger, min, max);
    }
}
