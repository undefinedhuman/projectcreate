package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;

public class TextFieldSetting<T> extends BaseSetting<T> {

    protected JTextField valueField;

    public TextFieldSetting(String key, T defaultValue, Parser<T> parser, Serializer<T> serializer) {
        super(key, defaultValue, parser, serializer);
        setContentHeight(Variables.DEFAULT_CONTENT_HEIGHT);
    }

    @Override
    protected void createValueUI(JPanel panel, int width) {
        panel.add(valueField = Tools.createTextField(serializer.serialize(getValue()), new Vector2i(0, 0), new Vector2i(width, getContentHeight()), s -> setValue(parser.parse(s))));
    }

    @Override
    protected void updateMenu(T value) {
        if(valueField != null)
            valueField.setText(serializer.serialize(value));
    }
}
