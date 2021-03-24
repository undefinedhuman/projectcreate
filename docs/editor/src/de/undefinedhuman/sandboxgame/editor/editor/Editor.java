package de.undefinedhuman.sandboxgame.editor.editor;

import javax.swing.*;
import java.awt.*;

public abstract class Editor {

    protected JPanel mainPanel, middlePanel, settingsPanel;

    public Editor(Container container) {
        mainPanel = new JPanel(null, true);
        settingsPanel = new JPanel(null, true);
        middlePanel = new JPanel(null, true);
        container.add(addPanel(mainPanel, "Main:", 20, 15, 820, 960));
        container.add(addPanel(middlePanel, "Type:", 860, 15, 200, 960));
        container.add(addPanel(settingsPanel, "Settings:", 1080, 15, 820, 960));
    }

    private JScrollPane addPanel(JPanel panel, String name, int x, int y, int width, int height) {
        panel.setPreferredSize(new Dimension(width - 25, height - 25));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(x, y, width, 990);
        scrollPane.setBorder(BorderFactory.createTitledBorder(name));
        return scrollPane;
    }

    public abstract void save();
    public abstract void load();

}
