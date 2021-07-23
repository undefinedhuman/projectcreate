package de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel;

import de.undefinedhuman.projectcreate.engine.settings.ui.factory.SettingsUIFactory;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;

public class InlinePanel extends AccordionPanel {

    public InlinePanel(String title, Color backgroundColor, JComponent content) {
        super(new RelativeLayout(RelativeLayout.X_AXIS));
        add(SettingsUtils.setPreferredSize(0, Variables.DEFAULT_CONTENT_HEIGHT, SettingsUIFactory.createSettingsTitleLabel("  " + title, backgroundColor)), 0.4f);
        add(SettingsUtils.setSize(0, Variables.DEFAULT_CONTENT_HEIGHT, content), 0.6f);
    }

    @Override
    public int getTotalHeight() {
        return Variables.DEFAULT_CONTENT_HEIGHT;
    }
}
