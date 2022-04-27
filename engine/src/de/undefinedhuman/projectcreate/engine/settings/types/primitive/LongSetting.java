package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class LongSetting extends NumberSetting<Long> {

    public LongSetting(String key, Long defaultValue) {
        super(key, defaultValue, Utils::isLong);
    }

    public LongSetting(String key, Long defaultValue, Long min, Long max) {
        super(key, defaultValue, Utils::isLong, min, max);
    }

}
