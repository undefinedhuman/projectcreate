package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Vector2Setting extends Setting {

    private JTextField xTextField, yTextField;

    public Vector2Setting(String key, Vector2 value) {
        super(SettingType.Vector2, key, value);
    }

    @Override
    public void load(FsFile parentDir, Object value) {
        if(!(value instanceof LineSplitter)) return;
        setValue(((LineSplitter) value).getNextVector2());
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString(key).writeVector2(getVector2());
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        xTextField = createTextField(getVector2().x, position, new Vector2(95, 25), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(xTextField.getText() == null || xTextField.getText().equalsIgnoreCase("")) return;
                setValue(new Vector2(Float.parseFloat(xTextField.getText()), getVector2().y));
            }
        });

        yTextField = createTextField(getVector2().y, new Vector2(position).add(105, 0), new Vector2(95, 25), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(yTextField.getText() == null || yTextField.getText().equalsIgnoreCase("")) return;
                setValue(new Vector2(getVector2().x, Float.parseFloat(yTextField.getText())));
            }
        });

        panel.add(xTextField);
        panel.add(yTextField);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(xTextField != null) xTextField.setText(String.valueOf(getVector2().x));
        if(yTextField != null) yTextField.setText(String.valueOf(getVector2().y));
    }

}