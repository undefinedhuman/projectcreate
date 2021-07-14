package de.undefinedhuman.projectcreate.engine.settings.ui.accordion;

import de.undefinedhuman.projectcreate.engine.settings.ui.event.BooleanChangeListener;
import de.undefinedhuman.projectcreate.engine.settings.ui.factory.SettingsUIFactory;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public class CollapsibleLabel extends JPanel {

    private CollapsibleButton toggleButton;
    private JLabel label;

    public CollapsibleLabel(String title, Color background, BooleanChangeListener visibilityToggle) {
        super(new BorderLayout());
        add(SettingsUtils
                .setPreferredSize(Variables.DEFAULT_CONTENT_HEIGHT, Variables.DEFAULT_CONTENT_HEIGHT,
                        toggleButton = new CollapsibleButton(background).addChangeListener(visibilityToggle)
                ), BorderLayout.WEST);
        add(label = SettingsUIFactory.createSettingsTitleLabel(title, background), BorderLayout.CENTER);
    }
}
