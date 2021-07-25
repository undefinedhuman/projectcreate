package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

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
    public void createSettingUI(Accordion accordion) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true));
        panel.add(xTextField = Tools.createTextField(String.valueOf(getValue().x), s -> setValue(new Vector2(Float.parseFloat(s), getValue().y))), 0.5f);
        panel.add(yTextField = Tools.createTextField(String.valueOf(getValue().x), s -> setValue(new Vector2(getValue().x, Float.parseFloat(s)))), 0.5f);
        accordion.addInlinePanel(key, panel);
    }

    @Override
    protected void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        LineSplitter splitter = (LineSplitter) value;
        setValue(splitter.getNextVector2());
    }

    @Override
    protected void updateMenu(Vector2 value) {
        if(xTextField == null || yTextField == null)
            return;
        xTextField.setText(String.valueOf(getValue().x));
        yTextField.setText(String.valueOf(getValue().y));
    }

}
