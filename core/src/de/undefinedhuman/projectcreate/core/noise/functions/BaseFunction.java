package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.util.function.Consumer;

public abstract class BaseFunction extends PanelObject<String> {

    public IntSetting priority = new IntSetting("priority", 0);

    public BaseFunction(String key) {
        setKey(key);
        addSettings(priority);
    }

    public JPanel createPanel(Consumer<JPanel> removeFunction) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));
        Accordion accordion = new Accordion(Variables.BACKGROUND_COLOR, false);
        getSettings().stream().filter(setting -> setting != priority).forEach(setting -> setting.createSettingUI(accordion));
        panel.add(SettingsUI.createButton("Remove", Variables.DEFAULT_CONTENT_HEIGHT, e -> removeFunction.accept(panel)));
        panel.add(accordion);
        return panel;
    }

    public abstract double calculateValue(double x, double y, double value);
}
