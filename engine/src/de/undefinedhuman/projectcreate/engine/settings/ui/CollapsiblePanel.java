package de.undefinedhuman.projectcreate.engine.settings.ui;

import de.undefinedhuman.projectcreate.engine.settings.ui.factory.SettingsUIFactory;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CollapsiblePanel extends JPanel {

    private int height = 0;
    private CollapsibleButton visibilityToggle;
    private JLabel titleLabel;
    private JPanel content;

    public CollapsiblePanel(JPanel content, int width, String title, Color background, Consumer<Boolean> visibilityChange) {
        super(null);
        setSize(width, Variables.DEFAULT_CONTENT_HEIGHT + content.getHeight());
        this.content = content;
        this.height = calculateHeight(true);
        titleLabel = (JLabel) SettingsUtils.setBounds(Variables.DEFAULT_CONTENT_HEIGHT, 0, width - Variables.DEFAULT_CONTENT_HEIGHT, Variables.DEFAULT_CONTENT_HEIGHT, SettingsUIFactory.createSettingsTitleLabel(title, background));
        visibilityToggle = (CollapsibleButton) SettingsUtils
                .setSize(Variables.DEFAULT_CONTENT_HEIGHT, Variables.DEFAULT_CONTENT_HEIGHT,
                        new CollapsibleButton(background)
                                .addChangeListener(value -> {
                                    content.setVisible(value);
                                    height = calculateHeight(value);
                                    setSize(width, height);
                                    visibilityChange.accept(value);
                                })
                );
        content.setLocation(0, Variables.DEFAULT_CONTENT_HEIGHT);
        add(titleLabel);
        add(visibilityToggle);
        add(content);
    }

    public int getTotalHeight() {
        return height;
    }

    private int calculateHeight(boolean visible) {
        int height = Variables.DEFAULT_CONTENT_HEIGHT;
        if(visible)
            height += content.getHeight();
        return height;
    }

}
