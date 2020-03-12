package me.gentlexd.sandboxeditor.editor.settings;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;

import javax.swing.*;

public class Vector2Setting extends Setting {

    private JTextField[] textFields;

    public Vector2Setting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        textFields = new JTextField[2];

        for(int i = 0; i < textFields.length; i++) {

            textFields[i] = new JTextField("0");
            textFields[i].setBounds((int) position.x + 110 + 35 * i, (int) position.y,40, 25);
            panel.add(textFields[i]);

        }

    }

    public Vector2Setting(JPanel panel, String name, Object value, boolean add) {

        super(panel, name, value, add);

        textFields = new JTextField[2];

        for(int i = 0; i < textFields.length; i++) {

            textFields[i] = new JTextField("0");
            textFields[i].setBounds((int) position.x + 110 + 35 * i, (int) position.y,40, 25);
            if(add) panel.add(textFields[i]);

        }

    }

    @Override
    public void setValue(Object value) {

        Vector2 vector = (Vector2) value;

        textFields[0].setText(String.valueOf((int) vector.x));
        textFields[1].setText(String.valueOf((int) vector.y));

    }

    @Override
    public Object getValue() {

        StringBuilder s = new StringBuilder();
        for(JTextField textField : textFields) s.append(textField.getText()).append(",");
        return s.toString();

    }

    public Vector2 getVector() {

        return new Vector2((int) Float.parseFloat(textFields[0].getText()), (int) Float.parseFloat(textFields[1].getText()));

    }

    @Override
    public void update() {

        for(int i = 0; i < textFields.length; i++) textFields[i].setBounds((int) position.x + 110 + 40 * i, (int) position.y,35,25);

    }

    @Override
    public void save(FileWriter writer) {

        for (JTextField textField : textFields) writer.writeFloat(Float.parseFloat(textField.getText()));

    }

    @Override
    public void load(FileReader reader, int id) {

        for (JTextField textField : textFields) textField.setText(String.valueOf(reader.getNextFloat()));

    }

    @Override
    protected void addGuiSetting() {

        for(JTextField textField : textFields) panel.add(textField);

    }

}
