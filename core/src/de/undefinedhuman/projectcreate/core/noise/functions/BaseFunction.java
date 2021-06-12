package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;

public abstract class BaseFunction extends PanelObject {

    public BaseFunction(String key) {
        setKey(key);
    }

    public JPanel createPanel(int width) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(key));
        int contentWidth = width - Variables.BORDER_WIDTH;
        int contentHeight = 0;
        for(Setting<?> setting : settings.getSettings())
            contentHeight += setting.getTotalHeight();
        JPanel contentPanel = new JPanel(null);
        contentPanel.setSize(contentWidth, contentHeight);
        Tools.addSettings(contentPanel, 0, 0, Variables.OFFSET, contentWidth, settings.getSettings().stream());
        panel.add(contentPanel);
        return panel;
    }

    public abstract double calculateValue(double x, double y, double value);
}
