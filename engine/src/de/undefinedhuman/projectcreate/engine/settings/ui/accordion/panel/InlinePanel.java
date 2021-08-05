package de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel;

import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public class InlinePanel extends AccordionPanel {

    public InlinePanel(String title, Color backgroundColor, JComponent content) {
        super(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true));
        add(SettingsUtils.setPreferredSize(0, Variables.DEFAULT_CONTENT_HEIGHT, SettingsUI.createSettingsTitleLabel(title, SwingConstants.CENTER, backgroundColor)), 0.5f);
        add(SettingsUtils.setSize(0, Variables.DEFAULT_CONTENT_HEIGHT, content), 0.5f);
    }

}
