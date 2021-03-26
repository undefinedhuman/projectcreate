package de.undefinedhuman.projectcreate.editor.editor;

import javax.swing.*;
import java.awt.*;

public abstract class Editor {

    protected JPanel mainPanel, middlePanel, settingsPanel;

    public Editor(Container container) {
        addPanel(container, mainPanel = new JPanel(null, true), "Main:", 20, 820);
        addPanel(container, middlePanel = new JPanel(null, true), "Type:", 860, 200);
        addPanel(container, settingsPanel = new JPanel(null, true), "Settings:", 1080, 820);
    }

    private void addPanel(Container container, JPanel panel, String name, int x, int width) {
        panel.setPreferredSize(new Dimension(width - 25, 960 - 25));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(x, 15, width, 990);
        scrollPane.setBorder(BorderFactory.createTitledBorder(name));
        container.add(scrollPane);
    }

    public abstract void save();
    public abstract void load();

}
