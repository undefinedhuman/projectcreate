package de.undefinedhuman.projectcreate.engine.settings.types;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class BooleanSetting extends Setting {

    public JCheckBox checkBox;
    private static final int CHECK_BOX_SIZE = Variables.DEFAULT_CONTENT_HEIGHT;

    public BooleanSetting(String key, boolean value) {
        super(key, value);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        checkBox = new JCheckBox();
        checkBox.setSelected(getBoolean());
        checkBox.setSize(CHECK_BOX_SIZE, CHECK_BOX_SIZE);
        checkBox.addActionListener(e -> value = checkBox.isSelected());
        panel.add(checkBox);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(checkBox != null) checkBox.setSelected(getBoolean());
    }

}
