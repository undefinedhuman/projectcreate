package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

import javax.swing.*;

public class BooleanSetting extends Setting {

    public JCheckBox checkBox;

    public BooleanSetting(String key, boolean value) {
        super(SettingType.Boolean, key, value);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        checkBox = new JCheckBox();
        checkBox.setSelected(getBoolean());
        checkBox.setBounds((int) position.x, (int) position.y, 25, 25);
        checkBox.addActionListener(e -> value = checkBox.isSelected());
        panel.add(checkBox);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(checkBox != null) checkBox.setSelected(getBoolean());
    }

}
