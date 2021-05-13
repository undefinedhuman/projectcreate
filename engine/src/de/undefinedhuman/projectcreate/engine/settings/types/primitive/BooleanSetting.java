package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class BooleanSetting extends Setting<Boolean> {

    public JCheckBox checkBox;
    private static final int CHECK_BOX_SIZE = Variables.DEFAULT_CONTENT_HEIGHT;

    public BooleanSetting(String key, boolean defaultValue) {
        super(key, defaultValue, value -> FileUtils.readBoolean(String.valueOf(value)));
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        checkBox = new JCheckBox();
        checkBox.setSelected(getValue());
        checkBox.setSize(CHECK_BOX_SIZE, CHECK_BOX_SIZE);
        checkBox.addActionListener(e -> setValue(checkBox.isSelected()));
        panel.add(checkBox);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(checkBox != null) checkBox.setSelected(getValue());
    }

}
