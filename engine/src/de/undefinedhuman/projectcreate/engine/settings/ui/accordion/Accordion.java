package de.undefinedhuman.projectcreate.engine.settings.ui.accordion;

import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.AccordionPanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.CollapsiblePanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.InlinePanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.factory.SettingsUIFactory;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Accordion extends JPanel {

    private ArrayList<AccordionPanel> contentPanels = new ArrayList<>();

    private Color backgroundColor;
    private JScrollPane contentScrollPanel;
    private JPanel contentPanel;

    public Accordion(Color backgroundColor) {
        this("", backgroundColor);
    }

    public Accordion(String title, Color backgroundColor) {
        super(new BorderLayout());
        setOpaque(true);
        this.backgroundColor = backgroundColor.darker();
        if(title != null && !title.equalsIgnoreCase(""))
            add(SettingsUtils.setPreferredSize(0, Variables.DEFAULT_CONTENT_HEIGHT, new CollapsibleLabel(title, this.backgroundColor, value -> {
                contentScrollPanel.setVisible(value);
                update();
            })), BorderLayout.NORTH);
        add(contentScrollPanel = SettingsUIFactory.createContentScrollPanel(contentPanel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS, 2).setFill(true)), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    }

    public void addInlinePanel(String title, JComponent content) {
        InlinePanel panel = new InlinePanel(title, backgroundColor, content);
        addPanel(panel);
    }

    public void addCollapsiblePanel(String title, JComponent content) {
        CollapsiblePanel panel = new CollapsiblePanel(title, backgroundColor, content, value -> update());
        addPanel(panel);
    }

    private void addPanel(AccordionPanel panel) {
        contentPanel.add(panel);
        contentPanels.add(panel);
        update();
    }

    public void update() {
        int currentContentHeight = 0;
        for(Component panel : contentPanel.getComponents()) {
            panel.setLocation(panel.getX(), currentContentHeight);
            if(!(panel instanceof CollapsiblePanel))
                continue;
            currentContentHeight += ((AccordionPanel) panel).getTotalHeight();
        }
        SettingsUtils.repaint(contentPanel, contentScrollPanel);
    }

    public void remove(AccordionPanel content) {
        if(!contentPanels.contains(content))
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
