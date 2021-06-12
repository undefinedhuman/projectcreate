package de.undefinedhuman.projectcreate.engine.settings.types.primitive;

import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.settings.types.BaseSetting;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public class BooleanSetting extends BaseSetting<Boolean> {

    public JCheckBox checkBox;
    private static final int CHECK_BOX_SIZE = Variables.DEFAULT_CONTENT_HEIGHT;

    public BooleanSetting(String key, boolean defaultValue) {
        super(key, defaultValue, value -> FileUtils.readBoolean(String.valueOf(value)), Object::toString);
    }

    @Override
    protected void createValueMenuComponents(JPanel panel, int width) {
        checkBox = new JCheckBox();
        checkBox.setSelected(getValue());
        checkBox.setSize(CHECK_BOX_SIZE, CHECK_BOX_SIZE);
        checkBox.addActionListener(e -> setValue(checkBox.isSelected()));
        panel.add(checkBox);
    }

    @Override
    protected void updateMenu(Boolean value) {
        if(checkBox != null)
            checkBox.setSelected(getValue());
    }

}
