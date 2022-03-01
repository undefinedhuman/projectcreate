package de.undefinedhuman.projectcreate.engine.settings.types.selection;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;

public class EnumSetting<T extends Enum<T>> extends SelectionSetting<T> {

    public EnumSetting(String key, T[] values, Parser<T> parser) {
        super(key, values, parser, Enum::name);
    }

    public EnumSetting(String key, T defaultValue, T[] values, Parser<T> parser) {
        super(key, defaultValue, values, parser, Enum::name);
    }

}
