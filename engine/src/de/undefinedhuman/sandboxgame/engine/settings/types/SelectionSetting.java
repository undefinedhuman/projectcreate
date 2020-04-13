package de.undefinedhuman.sandboxgame.engine.settings.types;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;

import javax.swing.*;

public class SelectionSetting extends Setting {

    private JComboBox<String> selection;
    private String[] values;

    public SelectionSetting(String key, Object[] values) {
        super(SettingType.Selection, key, values[0]);
        this.values = new String[values.length];
        for(int i = 0; i < values.length; i++) this.values[i] = values[i].toString();
    }

    @Override
    public void load(FsFile parentDir, LineSplitter splitter) {
        value = splitter.getNextString();
    }

    @Override
    public void save(FsFile parentDir, FileWriter writer) {
        writer.writeString(String.valueOf(value));
    }

    @Override
    protected void delete() {
        super.delete();
        values = new String[0];
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        selection = new JComboBox<>(values);
        selection.setBounds((int) position.x, (int) position.y, 200, 25);
        selection.setSelectedItem(value);
        selection.addActionListener(e -> setValue(selection.getSelectedItem()));
        panel.add(selection);
    }

    @Override
    protected void setValueInMenu(Object value) {
        if(selection != null) selection.setSelectedItem(value);
    }

}
