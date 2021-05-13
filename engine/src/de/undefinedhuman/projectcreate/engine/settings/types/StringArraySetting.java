package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StringArraySetting extends Setting<String[]> {

    public StringArraySetting(String key, String[] defaultValue) {
        super(key, defaultValue, value -> (String[]) value);
    }

    @Override
    public void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter)) return;
        LineSplitter splitter = (LineSplitter) value;
        String[] values = new String[splitter.getNextInt()];
        for(int i = 0; i < values.length; i++) values[i] = splitter.getNextString();
        setValue(values);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeInt(getValue().length);
        for(String s : getValue()) writer.writeString(s);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        valueField = createTextField(Tools.convertArrayToString(getValue()), new Vector2(0, 0), new Vector2(width, Variables.DEFAULT_CONTENT_HEIGHT), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(valueField.getText() == null || valueField.getText().equalsIgnoreCase("")) return;
                String[] array = valueField.getText().split(";");
                setValue(array);
            }
        });
        panel.add(valueField);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(valueField != null)
            valueField.setText(Tools.convertArrayToString(getValue()));
    }

}
