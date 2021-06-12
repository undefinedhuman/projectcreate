package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

import javax.swing.*;

public class Vector2ArraySetting extends TextFieldSetting<Vector2[]> {

    protected JTextField valueField;

    public Vector2ArraySetting(String key, Vector2[] defaultValue) {
        super(key, defaultValue, value -> {
            LineSplitter splitter = new LineSplitter(value, false);
            Vector2[] vector2s = new Vector2[splitter.getNextInt()];
            for(int i = 0; i < vector2s.length; i++)
                vector2s[i] = splitter.getNextVector2();
            return vector2s;
        }, value -> {
            LineWriter writer = new LineWriter(false);
            for(Vector2 vector : value)
                writer.writeVector2(vector);
            return writer.getData();
        });
    }

}
