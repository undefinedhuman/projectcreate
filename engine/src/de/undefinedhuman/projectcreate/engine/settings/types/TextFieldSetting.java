package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import javax.swing.*;

public class TextFieldSetting<T> extends BaseSetting<T> {

    protected static String DEFAULT_VALUE = "UNDEFINED";
    protected JTextField valueField;
    protected boolean canBeEmpty;

    public TextFieldSetting(String key, T defaultValue, Parser<T> parser, Serializer<T> serializer) {
        this(key, defaultValue, parser, serializer, false);
    }

    public TextFieldSetting(String key, T defaultValue, Parser<T> parser, Serializer<T> serializer, boolean canBeEmpty) {
        super(key, defaultValue, parser, serializer);
        this.canBeEmpty = canBeEmpty;
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        accordion.addInlinePanel(key, valueField = Utils.createTextField(getKey(), serializer.serialize(getValue()), s -> setValue(parser.parse(s)), canBeEmpty, DEFAULT_VALUE));
    }

    @Override
    protected void updateMenu(T value) {
        if(valueField != null)
            valueField.setText(serializer.serialize(value));
    }
}
