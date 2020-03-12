package me.gentlexd.sandboxeditor.entity;

import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;

import javax.swing.*;

public abstract class Component {

    protected JPanel panel;
    private String name;

    public Component(JPanel panel, String name) {

        this.panel = panel;
        this.name = name;

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
    public abstract void save(FileWriter writer);
    public abstract void load(FileReader reader, int id);
    public abstract void addMenuComponent();
    public abstract void removeMenuComponent();

}
