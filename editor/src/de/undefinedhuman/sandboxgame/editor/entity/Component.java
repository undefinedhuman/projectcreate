package de.undefinedhuman.sandboxgame.editor.entity;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.LineSplitter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Component {

    protected JPanel panel;
    private String name;

    protected ArrayList<Setting> settings;

    public Component(JPanel panel, String name) {

        this.panel = panel;
        this.name = name;

        settings = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(Vector2 position) {
        update();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void addSetting(Setting setting) {

        this.settings.add(setting);

    }

    public void addMenu() {

        addMenuComponent();
        panel.revalidate();
        panel.repaint();

    }

    public void removeMenu() {

        removeMenuComponent();
        panel.revalidate();
        panel.repaint();

    }

    public abstract void update();
    public void save(FileWriter writer) {

        writer.writeInt(settings.size());
        writer.nextLine();
        for(Setting setting : settings) setting.saveSetting(writer);

    }

    public void load(FileReader reader, int id) {

        HashMap<String, LineSplitter> settingsList = new HashMap<>();
        int sizeSettings = reader.getNextInt();
        for(int j = 0; j < sizeSettings; j++) {
            reader.nextLine();
            settingsList.put(reader.getNextString(), new LineSplitter(reader.nextLine(),true,";"));
        }

        for (Setting setting : settings) setting.loadSetting(settingsList, id);

    }

    public void addMenuComponent() {
        for(int i = 0; i < settings.size(); i++) settings.get(i).addToMenu(new Vector2(0,i * 30));
    }

    public void removeMenuComponent() {
        panel.removeAll();
    }

}
