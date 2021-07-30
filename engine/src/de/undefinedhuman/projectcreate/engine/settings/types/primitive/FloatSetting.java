package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.utils.Utils;

public class FloatSetting extends NumberSetting<Float> {
    public FloatSetting(String key, Float defaultValue) {
        super(key, defaultValue, Utils::isFloat);
    }

    public FloatSetting(String key, Float defaultValue, Float min, Float max) {
        super(key, defaultValue, Utils::isFloat, min, max);
    }
}
