package de.undefinedhuman.sandboxgame.editor.editor.settings;

import com.badlogic.gdx.math.Vector3;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;

import javax.swing.*;

public class Vector3Setting extends Setting {

    private JTextField[] textFields;

    public Vector3Setting(JPanel panel, String name, Object value) {

        super(panel, name, value);

        textFields = new JTextField[3];

        for(int i = 0; i < textFields.length; i++) {

            textFields[i] = new JTextField("0");
            textFields[i].setBounds((int) position.x + 110 + 25 * i, (int) position.y, 25, 25);
            panel.add(textFields[i]);

        }

    }

    @Override
    public void setValue(Object value) {

        Vector3 vector = (Vector3) value;

        textFields[0].setText(String.valueOf(vector.x));
        textFields[1].setText(String.valueOf(vector.y));
        textFields[2].setText(String.valueOf(vector.z));

    }

    @Override
    public Object getValue() {

        StringBuilder s = new StringBuilder();
        for(JTextField textField : textFields) s.append(textField.getText()).append(",");
        return s.toString();

    }

    @Override
    public void update() {

        for(int i = 0; i < textFields.length; i++) textFields[i].setBounds((int) position.x + 110 + 25 * i, (int) position.y, 25, 25);

    }

    @Override
    public void save(FileWriter writer) {

        for (JTextField textField : textFields) writer.writeFloat(Float.parseFloat(textField.getText()));

    }

    @Override
    public void load(LineSplitter splitter, int id) {

        for (JTextField textField : textFields) textField.setText(String.valueOf(splitter.getNextFloat()));

    }

    @Override
    protected void addGuiSetting() {

        for(JTextField textField : textFields) panel.add(textField);

    }

}
