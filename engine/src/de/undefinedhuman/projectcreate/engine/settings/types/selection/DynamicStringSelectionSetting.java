package de.undefinedhuman.projectcreate.engine.settings.types.selection;

import java.util.function.Supplier;

public class DynamicStringSelectionSetting extends DynamicSelectionSetting<String> {

    public DynamicStringSelectionSetting(String key, Supplier<String[]> getValues) {
        super(key, getValues, value -> value, value -> value);
    }

}
