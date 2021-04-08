package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

import javax.swing.*;

public class SelectionSetting extends Setting {

    public JComboBox<String> selection;
    private String[] values;

    public SelectionSetting(String key, Object[] values) {
        super(SettingType.Selection, key, values[0]);
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
        if(i < 0 || i >= values.length) return;
        setValue(values[i]);
        selection.setSelectedItem(values[i]);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        selection = new JComboBox<>(values);
        selection.setBounds((int) position.x, (int) position.y, 200, 25);
        if(hasValue(getString()))
            selection.setSelectedItem(getString());
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
