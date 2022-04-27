package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.util.ArrayList;

public class StringArraySetting extends TextFieldSetting<String[]> {

    public StringArraySetting(String key, String[] defaultValue) {
        super(key, defaultValue, value -> {
            if(value.equalsIgnoreCase(""))
                return new String[0];
            LineSplitter splitter = new LineSplitter(value, false);
            ArrayList<String> data = new ArrayList<>();
            while(splitter.hasMoreValues())
                data.add(splitter.getNextString());
            return data.toArray(new String[0]);
        }, value -> {
            if(value.length == 0)
                return "";
            StringBuilder builder = new StringBuilder();
            builder.append(value[0]);
            for(int i = 1; i < value.length; i++)
                builder.append(Variables.SEPARATOR).append(value[i]);
            return builder.toString();
        });
    }
}
