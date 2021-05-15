package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Tools;

public class LongSetting extends NumberSetting<Long> {

    public LongSetting(String key, Long defaultValue) {
        super(key, defaultValue, Tools::isLong, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public LongSetting(String key, Long defaultValue, Long min, Long max) {
        super(key, defaultValue, Tools::isLong, min, max);
    }

}
