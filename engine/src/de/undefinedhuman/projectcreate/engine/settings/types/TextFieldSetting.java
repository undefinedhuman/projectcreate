package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import javax.swing.*;

public class TextFieldSetting<T> extends BaseSetting<T> {

    protected JTextField valueField;

    public TextFieldSetting(String key, T defaultValue, Parser<T> parser, Serializer<T> serializer) {
        super(key, defaultValue, parser, serializer);
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        accordion.addInlinePanel(key, valueField = Utils.createTextField(serializer.serialize(getValue()), s -> setValue(parser.parse(s))));
    }

    @Override
    protected void updateMenu(T value) {
        if(valueField != null)
            valueField.setText(serializer.serialize(value));
    }
}
