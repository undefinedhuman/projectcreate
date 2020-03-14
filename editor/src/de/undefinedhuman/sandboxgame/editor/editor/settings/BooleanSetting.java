package de.undefinedhuman.sandboxgame.editor.editor.settings;

import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;

import javax.swing.*;

public class BooleanSetting extends Setting {

    private JCheckBox checkBox;

    public BooleanSetting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        checkBox = new JCheckBox();
        checkBox.setSelected(Boolean.parseBoolean(String.valueOf(value)));
        checkBox.setBounds((int) position.x + 110, (int) position.y, 25, 25);
        panel.add(checkBox);

    }

    public BooleanSetting(JPanel panel, String name, Object value, boolean add) {

        super(panel, name, value, add);

        checkBox = new JCheckBox();
        checkBox.setSelected(Boolean.parseBoolean(String.valueOf(value)));
        checkBox.setBounds((int) position.x + 110, (int) position.y, 25, 25);
        if(add) panel.add(checkBox);

    }

    @Override
    public void setValue(Object value) {

        super.setValue(value);

        if(Boolean.parseBoolean(String.valueOf(value))) checkBox.setSelected(true);
        else checkBox.setSelected(false);

    }

    @Override
    public Object getValue() {

        return checkBox.isSelected();

    }

    @Override
    public void update() {

        checkBox.setBounds((int) position.x + 110, (int) position.y, 25, 25);

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeBoolean(checkBox.isSelected());

    }

    @Override
    public void load(LineSplitter splitter, int id) {

        boolean value = splitter.getNextBoolean();
        checkBox.setSelected(value);

    }

    @Override
    protected void addGuiSetting() {
        panel.add(checkBox);
    }

}
