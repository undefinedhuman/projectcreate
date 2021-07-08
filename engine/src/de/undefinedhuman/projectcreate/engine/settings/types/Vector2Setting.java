package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;

public class Vector2Setting extends Setting<Vector2> {

    private JTextField xTextField, yTextField;

    public Vector2Setting(String key, Vector2 defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected void saveValue(FileWriter writer) {
        writer.writeVector2(getValue());
    }

    @Override
    protected void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        LineSplitter splitter = (LineSplitter) value;
        setValue(splitter.getNextVector2());
    }

    @Override
    protected void createValueUI(JPanel panel, int width) {
        int textFieldWidth = (int) (width/2f - Variables.OFFSET/2f);
        panel.add(xTextField = Tools.createTextField(String.valueOf(getValue().x), new Vector2i(0, 0), new Vector2i(textFieldWidth, Variables.DEFAULT_CONTENT_HEIGHT), s -> setValue(new Vector2(Float.parseFloat(s), getValue().y))));
        panel.add(yTextField = Tools.createTextField(String.valueOf(getValue().x), new Vector2i(width/2f + Variables.OFFSET/2f, 0), new Vector2i(textFieldWidth, Variables.DEFAULT_CONTENT_HEIGHT), s -> setValue(new Vector2(getValue().x, Float.parseFloat(s)))));
    }

    @Override
    protected void updateMenu(Vector2 value) {
        if(xTextField == null || yTextField == null)
            return;
        xTextField.setText(String.valueOf(getValue().x));
        yTextField.setText(String.valueOf(getValue().y));
    }

}
