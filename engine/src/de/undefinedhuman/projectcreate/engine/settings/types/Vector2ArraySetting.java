package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.util.ArrayList;

public class Vector2ArraySetting extends TextFieldSetting<Vector2[]> {

    public Vector2ArraySetting(String key, Vector2[] defaultValue) {
        super(key, defaultValue, value -> {
            if(value.equalsIgnoreCase(""))
                return new Vector2[0];
            LineSplitter splitter = new LineSplitter(value, false);
            ArrayList<Vector2> data = new ArrayList<>();
            while(splitter.hasMoreValues())
                data.add(splitter.getNextVector2());
            return data.toArray(new Vector2[0]);
        }, value -> {
            if(value.length == 0)
                return "";
            StringBuilder builder = new StringBuilder();
            builder.append(value[0].x).append(Variables.SEPARATOR).append(value[0].y);
            for(int i = 1; i < value.length; i++)
                builder.append(";").append(value[i].x).append(Variables.SEPARATOR).append(value[i].y);
            return builder.toString();
        });
    }

}
