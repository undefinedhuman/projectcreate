package de.undefinedhuman.sandboxgame.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StringArraySetting extends Setting {

    private JTextField valueField;

    public StringArraySetting(String key, String[] value) {
        super(SettingType.StringArray, key, value);
    }

    @Override
    public void load(FsFile parentDir, LineSplitter splitter) {
        String[] strings = new String[splitter.getNextInt()];
        for(int i = 0; i < strings.length; i++) strings[i] = splitter.getNextString();
        value = strings;
    }

    @Override
    public void save(FsFile parentDir, FileWriter writer) {
        writer.writeInt(getStringArray().length);
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
