package de.undefinedhuman.projectcreate.engine.settings.ui.factory;

import javax.swing.*;
import java.awt.*;

public class SettingsUIFactory {

    public static JLabel createSettingsTitleLabel(String title, Color color) {
        return createSettingsTitleLabel(title, SwingConstants.LEFT, color);
    }

    public static JLabel createSettingsTitleLabel(String title, int orientation, Color color) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(orientation);
        titleLabel.setBackground(color);
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        titleLabel.setOpaque(true);
        return titleLabel;
    }

    public static JScrollPane createContentScrollPanel(JPanel contentPanel, int vst, int hst) {
        JScrollPane settingsScrollableContainer = new JScrollPane(contentPanel, vst, hst);
        settingsScrollableContainer.setBorder(null);
        settingsScrollableContainer.getVerticalScrollBar().setUnitIncrement(8);
        return settingsScrollableContainer;
    }

}
