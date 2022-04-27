package de.undefinedhuman.projectcreate.engine.settings.types.selection;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;
import de.undefinedhuman.projectcreate.engine.settings.types.BaseSetting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;

import javax.swing.*;
import java.awt.*;

public class SelectionSetting<T> extends BaseSetting<T> {

    public JComboBox<T> selection;
    private T[] values;

    public SelectionSetting(String key, T[] values, Parser<T> parser, Serializer<T> serializer) {
        this(key, values[0], values, parser, serializer);
    }

    public SelectionSetting(String key, T defaultValue, T[] values, Parser<T> parser, Serializer<T> serializer) {
        super(key, defaultValue, parser, serializer);
        this.values = values;
    }

    @Override
    protected void delete() {
        super.delete();
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        selection = new JComboBox<>(values);
        selection.setFont(selection.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        if(hasValue(getValue()))
            selection.setSelectedItem(getValue());
        selection.addActionListener(e -> {
            if(selection.getSelectedItem() == null)
                return;
            setValue((T) selection.getSelectedItem());
        });
        accordion.addInlinePanel(key, selection);
    }

    @Override
    protected void updateMenu(T value) {
        if(selection != null)
            selection.setSelectedItem(serializer.serialize(value));
    }

    private boolean hasValue(T value) {
        for(T s : values)
            if(s.equals(value))
                return true;
        return false;
    }

}
