package de.undefinedhuman.sandboxgame.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

import javax.swing.*;

public class BooleanSetting extends Setting {

    private JCheckBox checkBox;

    public BooleanSetting(String key, boolean value) {
        super(SettingType.Boolean, key, value);
    }

    @Override
    public void load(FsFile parentDir, LineSplitter splitter) {
        value = splitter.getNextBoolean();
    }

    @Override
    public void save(FsFile parentDir, FileWriter writer) {
        writer.writeBoolean(getBoolean());
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
