package de.undefinedhuman.projectcreate.engine.settings.ui;

import de.undefinedhuman.projectcreate.engine.settings.ui.factory.SettingsUIFactory;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Accordion extends JPanel {

    private HashMap<JPanel, CollapsiblePanel> contentPanels = new HashMap<>();

    private Color backgroundColor;
    private JScrollPane contentScrollPanel;
    private JPanel contentPanel;
    private int width;

    public Accordion(String title, int width, int height, Color backgroundColor) {
        super(null);
        setSize(width, height);
        setOpaque(true);
        contentScrollPanel = (JScrollPane) SettingsUtils
                .setBounds(
                        0,
                        Variables.DEFAULT_CONTENT_HEIGHT,
                        width,
                        height - Variables.DEFAULT_CONTENT_HEIGHT,
                        SettingsUIFactory.createContentScrollPanel(
                                (JPanel) SettingsUtils.setBounds(0, 0, width, 10, contentPanel = new JPanel(null)), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
                );
        this.width = width;
        this.backgroundColor = backgroundColor;
        add(contentScrollPanel);
        add(SettingsUtils.setSize(Variables.DEFAULT_CONTENT_HEIGHT, Variables.DEFAULT_CONTENT_HEIGHT, SettingsUIFactory.createVisibilityToggle(backgroundColor.darker(), value -> {
            setSize(width, value ? height : Variables.DEFAULT_CONTENT_HEIGHT);
            update();
        })));
        add(SettingsUtils.setBounds(Variables.DEFAULT_CONTENT_HEIGHT, 0, width - Variables.DEFAULT_CONTENT_HEIGHT, Variables.DEFAULT_CONTENT_HEIGHT, SettingsUIFactory.createSettingsTitleLabel(title, backgroundColor.darker())));
    }

    public void addPanel(String title, JPanel content) {
        CollapsiblePanel panel = new CollapsiblePanel(content, width, title, backgroundColor, value -> update());
        contentPanel.add(panel);
        contentPanels.put(content, panel);
        update();
    }

    private void update() {
        int currentContentHeight = 0;
        for(Component panel : contentPanel.getComponents()) {
            panel.setLocation(panel.getX(), currentContentHeight);
            currentContentHeight += ((CollapsiblePanel) panel).getTotalHeight();
        }
        contentPanel.setPreferredSize(new Dimension(contentPanel.getWidth(), currentContentHeight));
        SettingsUtils.repaint(contentPanel, contentScrollPanel);
    }

    public void remove(JPanel content) {
        if(!contentPanels.containsKey(content))
            return;
        contentPanels.remove(content);
        contentPanel.remove(content);
        update();
    }

    public void removeAll() {
        this.contentPanels.clear();
        contentPanel.removeAll();
        update();
    }

}
