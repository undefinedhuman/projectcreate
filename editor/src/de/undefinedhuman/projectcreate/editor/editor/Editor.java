package de.undefinedhuman.projectcreate.editor.editor;

import javax.swing.*;
import java.awt.*;

public abstract class Editor {

    protected JPanel leftPanel, middlePanel, rightPanel;

    public Editor(Container container) {
        leftPanel = addPanel(container, "Main:", 20, 820);
        middlePanel = addPanel(container, "Type:", 860, 200);
        rightPanel = addPanel(container, "Settings:", 1080, 820);
    }

    private JPanel addPanel(Container container, String name, int x, int width) {
        JPanel panel = new JPanel(null, true);
        panel.setPreferredSize(new Dimension(width - 25, 960 - 25));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(x, 15, width, 990);
        scrollPane.setBorder(BorderFactory.createTitledBorder(name));
        container.add(scrollPane);
        return panel;
    }

    public abstract void save();
    public abstract void load();

}
