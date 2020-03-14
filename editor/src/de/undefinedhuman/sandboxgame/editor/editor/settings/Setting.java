package de.undefinedhuman.sandboxgame.editor.editor.settings;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class Setting {

    protected JPanel panel;

    private String name;
    private Object value;
    Vector2 position;

    private JLabel nameLabel;

    Setting(JPanel panel, String name, Object value) {
        this(panel, name, value,true);
    }

    Setting(JPanel panel, String name, Object value, boolean add) {

        this.panel = panel;

        this.name = name;
        this.value = value;
        this.position = new Vector2(0,0);

        nameLabel = new JLabel(name+":", SwingConstants.CENTER);
        nameLabel.setBounds((int) position.x, (int) position.y, 100, 25);
        nameLabel.setBackground(Color.LIGHT_GRAY);
        nameLabel.setOpaque(true);
        if(add) panel.add(nameLabel);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameLabel.setText(name);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setPosition(Vector2 position) {

        this.position = position;
        nameLabel.setBounds((int) position.x, (int) position.y, 100, 25);
        update();

    }

    public JPanel getPanel() {
        return panel;
    }

    public void addToMenu(Vector2 pos) {

        addGui();
        setPosition(pos);
        update();

    }

    public void addGui() {

        panel.add(nameLabel);
        addGuiSetting();

    }

    public abstract void update();

    public void saveSetting(FileWriter writer) {

        writer.writeString(name);
        writer.nextLine();
        save(writer);
        writer.nextLine();

    }

    public void loadSetting(HashMap<String, LineSplitter> s, int id) {

        if(s.containsKey(name)) load(s.get(name), id);

    }

    public abstract void save(FileWriter writer);
    public abstract void load(LineSplitter splitter, int id);
    protected abstract void addGuiSetting();

}
