package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.settings.interfaces.Getter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class SelectionSetting<T> extends Setting<T> {

    public JComboBox<String> selection;
    private String[] values;

    public SelectionSetting(String key, Object[] values, Getter<T> getter) {
        super(key, values[0], getter);
        this.values = new String[values.length];
        for(int i = 0; i < values.length; i++)
            this.values[i] = values[i].toString();
    }

    @Override
    protected void delete() {
        super.delete();
        values = new String[0];
    }

    public void setSelected(int i) {
        if(!Tools.isInRange(i, 0, values.length-1))
            return;
        setValue(values[i]);
        selection.setSelectedItem(values[i]);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        selection = new JComboBox<>(values);
        selection.setSize(width, Variables.DEFAULT_CONTENT_HEIGHT);
        if(hasValue(getValue().toString()))
            selection.setSelectedItem(getValue());
        selection.addActionListener(e -> setValue(selection.getSelectedItem()));
        panel.add(selection);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(selection != null)
            selection.setSelectedItem(value.toString());
    }

    private boolean hasValue(String value) {
        for(String s : values)
            if(s.equals(value))
                return true;
        return false;
    }

}
