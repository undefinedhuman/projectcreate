package de.undefinedhuman.projectcreate.core.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.core.engine.file.FsFile;
import de.undefinedhuman.projectcreate.core.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.core.engine.utils.Tools;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StringArraySetting extends Setting {

    private JTextField valueField;

    public StringArraySetting(String key, String[] value) {
        super(SettingType.StringArray, key, value);
    }

    @Override
    public void load(FsFile parentDir, Object value) {
        if(!(value instanceof LineSplitter)) return;
        LineSplitter splitter = (LineSplitter) value;
        String[] values = new String[splitter.getNextInt()];
        for(int i = 0; i < values.length; i++) values[i] = splitter.getNextString();
        setValue(values);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeInt(getStringArray().length);
        for(String s : getStringArray()) writer.writeString(s);
    }

    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        valueField = createTextField(Tools.convertArrayToString(getStringArray()), position, new Vector2(200, 25), new KeyAdapter() {
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
        if(valueField != null) valueField.setText(Tools.convertArrayToString(getStringArray()));
    }

}
