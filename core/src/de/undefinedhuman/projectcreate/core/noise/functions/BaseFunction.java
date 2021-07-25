package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.util.function.Consumer;

public abstract class BaseFunction extends PanelObject {

    public BaseFunction(String key) {
        setKey(key);
    }

    public JPanel createPanel(int width, Consumer<JPanel> removeFunction) {
        JPanel panel = new JPanel();

        JButton remove = new JButton("Remove");
        remove.setBounds(0, Variables.DEFAULT_CONTENT_HEIGHT, width, Variables.DEFAULT_CONTENT_HEIGHT);

        remove.addActionListener(e -> removeFunction.accept(panel));

        for(Setting<?> setting : settings)
            width += setting.getTotalHeight();

        JPanel contentPanel = new JPanel(null);
        contentPanel.setSize(width, Variables.DEFAULT_CONTENT_HEIGHT*2);
        contentPanel.add(remove);
        //contentPanel.add(SettingsUIFactory.createSettingsTitleLabel(key, width, Variables.DEFAULT_CONTENT_HEIGHT, Variables.BACKGROUND_COLOR.darker().darker()));
        //contentPanel.add(SettingsUIFactory.createSettingsPanel(settings.getSettings(), 0, Variables.DEFAULT_CONTENT_HEIGHT*2, width, Variables.OFFSET));
        panel.add(contentPanel);
        return panel;
    }

    public abstract double calculateValue(double x, double y, double value);
}
