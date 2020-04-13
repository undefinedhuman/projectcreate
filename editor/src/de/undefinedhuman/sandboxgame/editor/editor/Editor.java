package de.undefinedhuman.sandboxgame.editor.editor;

import javax.swing.*;
import java.awt.*;

public abstract class Editor {

    protected JPanel mainPanel, settingsPanel;

    public Editor(Container container) {
        mainPanel = new JPanel(null, true);
        settingsPanel = new JPanel(null, true);
        container.add(addPanel(mainPanel, "Main:", new Dimension(480, 260), 25, 25));
        container.add(addPanel(settingsPanel, "Settings:", new Dimension(400, 580), 780, 25));
    }

    private JScrollPane addPanel(JPanel panel, String name, Dimension panelSize, int x, int y) {
        panel.setPreferredSize(panelSize);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(x, y, 460, 620);
        scrollPane.setBorder(BorderFactory.createTitledBorder(name));
        return scrollPane;
    }

    public abstract void save();
    public abstract void load();

}
