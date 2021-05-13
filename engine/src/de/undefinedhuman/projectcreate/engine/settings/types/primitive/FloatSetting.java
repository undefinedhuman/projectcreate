package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.settings.Setting;

public class FloatSetting extends Setting<Float> {

    public FloatSetting(String key, Object defaultValue) {
        super(key, defaultValue, value -> Float.parseFloat(String.valueOf(value)));
    }

}
