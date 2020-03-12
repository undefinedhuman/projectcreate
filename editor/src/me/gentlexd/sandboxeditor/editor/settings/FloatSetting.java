package me.gentlexd.sandboxeditor.editor.settings;

import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;

import javax.swing.*;

public class FloatSetting extends Setting {

    private JTextField textField;

    public FloatSetting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        textField = new JTextField(String.valueOf(value));
        textField.setBounds((int) position.x + 110, (int) position.y, 200, 25);
        panel.add(textField);

    }

    public FloatSetting(JPanel panel, String name, Object value, boolean add) {

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

        textField.setBounds((int) position.x + 110, (int) position.y, 200, 25);

    }

    @Override
    public void save(FileWriter writer) {

        writer.writeFloat(Float.parseFloat(textField.getText()));

    }

    @Override
    public void load(FileReader reader, int id) {

        String value = String.valueOf(reader.getNextFloat());
        textField.setText(value);

    }

    @Override
    protected void addGuiSetting() {

        panel.add(textField);

    }

}
