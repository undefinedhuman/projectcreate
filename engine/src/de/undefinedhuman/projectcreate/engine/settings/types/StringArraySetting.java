package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class StringArraySetting extends TextFieldSetting<String[]> {

    public StringArraySetting(String key, String[] defaultValue) {
        super(key, defaultValue, value -> {
            LineSplitter splitter = new LineSplitter(value, false);
            String[] values = new String[splitter.getNextInt()];
            for(int i = 0; i < values.length; i++)
                values[i] = splitter.getNextString();
            return values;
        }, value -> {
            StringBuilder builder = new StringBuilder(value.length);
            for(String s : value)
                builder.append(s).append(Variables.SEPARATOR);
            return builder.toString();
        });
    }

}
