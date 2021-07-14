package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.types.TextFieldSetting;

public abstract class NumberSetting<T extends Number & Comparable<T>> extends TextFieldSetting<T> {

    public NumberSetting(String key, T defaultValue, Parser<T> parse) {
        super(key, defaultValue, value -> {
            T parsedValue = parse.parse(String.valueOf(value));
            if (parsedValue == null) {
                Log.error("Error while parsing: \"" + value + "\"");
                return defaultValue;
            }
            return parsedValue;
        }, String::valueOf);
    }

    public NumberSetting(String key, T defaultValue, Parser<T> parse, T min, T max) {
        super(key, defaultValue, value -> {
            T parsedValue = parse.parse(String.valueOf(value));
            if(parsedValue == null) {
                Log.error("Error while parsing: \"" + value + "\"");
                return defaultValue;
            }
            if(parsedValue.compareTo(min) < 0 || parsedValue.compareTo(max) > 0) {
                Log.error("Out of range: Range [" + min + ", " + max + "], Value: " + parsedValue);
                return defaultValue;
            }
            return parsedValue;
        }, String::valueOf);
        setMenuTitle(key + ", Range [" + min + ", " + max + "]");
    }

}
