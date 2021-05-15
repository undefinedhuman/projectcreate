package de.undefinedhuman.projectcreate.editor.editor;

import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public abstract class Editor {

    public Editor(Container container) {
        Variables.IS_EDITOR = true;
    }

    protected void addPanel(Container container, JScrollPane... scrollPanes) {
        for(JScrollPane scrollPane : scrollPanes)
            container.add(scrollPane);
    }

    protected JPanel createJPanel(int width, int height) {
        JPanel panel = new JPanel(null, true);
        panel.setSize(new Dimension(width - Variables.BORDER_WIDTH, height - Variables.BORDER_HEIGHT));
        return panel;
    }

    protected JScrollPane createScrollPane(JPanel content, String title, int x, int y, int width, int height, int vsbPolicy, int hsbPolicy) {
        JScrollPane scrollPane = new JScrollPane(content, vsbPolicy, hsbPolicy);
        scrollPane.setBounds(x, y, width, height);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        return scrollPane;
    }

    public abstract void save();
    public abstract void load();

}
