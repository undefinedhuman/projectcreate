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

public class Vector2ArraySetting extends Setting {

    private JTextField valueField;

    public Vector2ArraySetting(String key, Vector2[] value) {
        super(SettingType.Vector2Array, key, value);
    }

    @Override
    public void load(FsFile parentDir, LineSplitter splitter) {
        Vector2[] vectors = new Vector2[splitter.getNextInt()];
        for(int i = 0; i < vectors.length; i++) vectors[i] = splitter.getNextVector2();
        setValue(vectors);
    }

    @Override
    public void save(FsFile parentDir, FileWriter writer) {
        writer.writeInt(getVector2Array().length);
        for(Vector2 vector : getVector2Array()) writer.writeVector2(vector);
    }

    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        valueField = createTextField(Tools.convertArrayToString(getVector2Array()), position, new Vector2(200, 25), new KeyAdapter() {
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
