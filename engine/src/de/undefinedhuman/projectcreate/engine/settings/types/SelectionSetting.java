package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class SelectionSetting<T> extends BaseSetting<T> {

    public JComboBox<String> selection;
    private String[] values;

    public SelectionSetting(String key, T[] values, Parser<T> parser, Serializer<T> serializer) {
        super(key, values[0], parser, serializer);
        this.values = new String[values.length];
        for(int i = 0; i < values.length; i++)
            this.values[i] = serializer.serialize(values[i]);
    }

    @Override
    protected void delete() {
        super.delete();
        values = new String[0];
    }

    @Override
    protected void createValueMenuComponents(JPanel panel, int width) {
        selection = new JComboBox<>(values);
        selection.setSize(width, Variables.DEFAULT_CONTENT_HEIGHT);
        if(hasValue(serializer.serialize(getValue())))
            selection.setSelectedItem(getValue());
        selection.addActionListener(e -> {
            if(selection.getSelectedItem() == null)
                return;
            setValue(parser.parse(selection.getSelectedItem().toString()));
        });
        panel.add(selection);
    }

    @Override
    protected void updateMenu(T value) {
        if(selection != null)
            selection.setSelectedItem(serializer.serialize(value));
    }

    private boolean hasValue(String value) {
        for(String s : values)
            if(s.equals(value))
                return true;
        return false;
    }

}
