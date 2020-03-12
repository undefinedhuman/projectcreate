package de.undefinedhuman.sandboxgame.editor.editor.settings;

import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;

import javax.swing.*;

public class  IntSetting extends Setting {

    private JTextField textField;
    private int width = 200;

    public IntSetting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        textField = new JTextField(String.valueOf(value));
        textField.setBounds((int) position.x + 110, (int) position.y, 200, 25);
        panel.add(textField);

    }

    public IntSetting(JPanel panel, String name, Object value, boolean add) {

        super(panel, name, value, add);

        textField = new JTextField(String.valueOf(value));
        textField.setBounds((int) position.x + 110, (int) position.y, 200, 25);
        if(add) panel.add(textField);

    }

    @Override
    public void setValue(Object value) {

        super.setValue(value);
        textField.setText(String.valueOf(value));

    }

    @Override
    public Object getValue() {

        return textField.getText();

    }

    @Override
    public void update() {

        textField.setBounds((int) position.x + 110, (int) position.y, width, 25);

    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void save(FileWriter writer) {

        writer.writeInt(Integer.parseInt(textField.getText()));

    }

    @Override
    public void load(LineSplitter splitter, int id) {

        String value = String.valueOf(splitter.getNextInt());
        textField.setText(value);

    }

    @Override
    protected void addGuiSetting() {

        panel.add(textField);

    }

}
