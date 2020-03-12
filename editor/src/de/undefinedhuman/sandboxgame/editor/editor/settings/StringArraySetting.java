package de.undefinedhuman.sandboxgame.editor.editor.settings;

import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;

import javax.swing.*;

public class StringArraySetting extends Setting {

    private JTextField textField;

    public StringArraySetting(JPanel panel,String name, Object value) {

        this(panel, name, value,true);

    }

    public StringArraySetting(JPanel panel, String name, Object value, boolean add) {

        super(panel, name, value, add);

        textField = new JTextField(String.valueOf(value));
        textField.setBounds((int) position.x + 110, (int) position.y,200, 25);
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

    public void setWidth(float width) {
        this.textField.setSize((int) width,25);
    }

    @Override
    public void update() {

        textField.setBounds((int) position.x + 110, (int) position.y, 200, 25);

    }

    @Override
    public void save(FileWriter writer) {

        String[] s = textField.getText().split(";");
        writer.writeInt(s.length);
        for(String s1 : s) writer.writeString(s1);

    }

    @Override
    public void load(LineSplitter splitter, int id) {

        int size = splitter.getNextInt();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; i++) builder.append(splitter.getNextString()).append(i < size-1 ? ";" : "");
        textField.setText(builder.toString());

    }

    @Override
    protected void addGuiSetting() {

        panel.add(textField);

    }

}
