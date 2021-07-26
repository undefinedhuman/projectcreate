package de.undefinedhuman.projectcreate.engine.settings.ui.accordion;

import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.AccordionPanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.CollapsiblePanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.ContentPanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.InlinePanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Accordion extends JPanel {

    private ArrayList<AccordionPanel> accordionPanels = new ArrayList<>();
    private HashMap<JComponent, AccordionPanel> contentPanels = new HashMap<>();

    private Color backgroundColor;
    private JScrollPane contentScrollPanel;
    private JPanel contentPanel;

    public Accordion(Color backgroundColor) {
        this("", backgroundColor, true);
    }

    public Accordion(Color backgroundColor, boolean scrollable) {
        this("", backgroundColor, scrollable);
    }

    public Accordion(String title, Color backgroundColor) {
        this(title, backgroundColor, true);
    }

    public Accordion(String title, Color backgroundColor, boolean scrollable) {
        super(new BorderLayout());
        setOpaque(true);
        this.backgroundColor = backgroundColor.darker();
        if(title != null && !title.equalsIgnoreCase(""))
            add(SettingsUtils.setPreferredSize(0, Variables.DEFAULT_CONTENT_HEIGHT, new CollapsibleLabel(title, this.backgroundColor, value -> {
                if(contentScrollPanel != null)contentScrollPanel.setVisible(value);
                else contentPanel.setVisible(value);
                update();
            })), BorderLayout.NORTH);
        contentPanel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));
        add(scrollable ? contentScrollPanel = SettingsUI.createContentScrollPanel(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) : contentPanel, BorderLayout.CENTER);
    }

    public void addInlinePanel(String title, JComponent content) {
        InlinePanel panel = new InlinePanel(title, backgroundColor.darker(), content);
        addPanel(null, panel);
    }

    public void addCollapsiblePanel(String title, JComponent content) {
        CollapsiblePanel panel = new CollapsiblePanel(title, backgroundColor, content, value -> update());
        addPanel(content, panel);
    }

    public AccordionPanel addContentPanel(String title, JComponent content) {
        ContentPanel panel = new ContentPanel(title, backgroundColor.darker(), content);
        addPanel(content, panel);
        return panel;
    }

    private void addPanel(JComponent content, AccordionPanel panel) {
        contentPanel.add(panel);
        accordionPanels.add(panel);
        if(content != null)
            contentPanels.put(content, panel);
        update();
    }

    public void update() {
        SettingsUtils.repaint(contentPanel, contentScrollPanel);
    }

    public void remove(AccordionPanel content) {
        if(!accordionPanels.contains(content))
            return;
        accordionPanels.remove(content);
        contentPanel.remove(content);
        update();
    }

    public AccordionPanel removeContent(JPanel content) {
        AccordionPanel accordionPanel = contentPanels.get(content);
        if(accordionPanel == null)
            return null;
        accordionPanels.remove(accordionPanel);
        contentPanel.remove(accordionPanel);
        contentPanels.remove(content);
        update();
        return accordionPanel;
    }

    public void removeAll() {
        this.accordionPanels.clear();
        contentPanel.removeAll();
        contentPanels.clear();
        update();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
