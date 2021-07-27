package de.undefinedhuman.projectcreate.engine.settings.ui.ui;

import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsUI {

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

    public static JButton createButton(String text, int preferredHeight, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        if(preferredHeight != 0) button.setPreferredSize(new Dimension(0, preferredHeight));
        button.addActionListener(listener);
        return button;
    }

    public static Accordion createAccordion(Accordion accordion, String title, Color backgroundColor, boolean scrollable) {
        return new Accordion(title, backgroundColor, scrollable) {
            @Override
            public void update() {
                super.update();
                accordion.update();
            }
        };
    }

}
