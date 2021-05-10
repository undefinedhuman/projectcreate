package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Vector2ArraySetting extends Setting {

    protected JTextField valueField;

    public Vector2ArraySetting(String key, Vector2[] value) {
        super(SettingType.Vector2Array, key, value);
    }

    public Vector2ArraySetting(SettingType type, String key, Vector2[] value) {
        super(type, key, value);
    }

    @Override
    public void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter)) return;
        LineSplitter splitter = (LineSplitter) value;
        Vector2[] vectors = new Vector2[splitter.getNextInt()];
        for(int i = 0; i < vectors.length; i++) vectors[i] = splitter.getNextVector2();
        setValue(vectors);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeInt(getVector2Array().length);
        for(Vector2 vector : getVector2Array()) writer.writeVector2(vector);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        valueField = createTextField(Tools.convertArrayToString(getVector2Array()), new Vector2(0, 0), new Vector2(width, Variables.DEFAULT_CONTENT_HEIGHT), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(valueField.getText() == null || valueField.getText().equalsIgnoreCase("")) return;
                String[] array = valueField.getText().split(";");
                Vector2[] vectorArray = new Vector2[array.length/2];
                for(int i = 0; i < vectorArray.length; i++) vectorArray[i] = new Vector2(Float.parseFloat(array[i*2]), Float.parseFloat(array[i*2+1]));
                setValue(vectorArray);
            }
        });
        panel.add(valueField);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(valueField != null) valueField.setText(Tools.convertArrayToString(getVector2Array()));
    }

}
