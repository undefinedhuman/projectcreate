package de.undefinedhuman.projectcreate.engine.settings.ui.factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SettingsUIFactory {

    public static JLabel createSettingsTitleLabel(String title, Color color) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBackground(color);
        titleLabel.setOpaque(true);
        return titleLabel;
    }

    public static JLabel createVisibilityToggle(Color color, Consumer<Boolean> isVisible) {
        JLabel toggleButton = new JLabel("▼");
        toggleButton.setBackground(color);
        toggleButton.setOpaque(true);
        toggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        AtomicBoolean visible = new AtomicBoolean(true);
        toggleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                visible.set(!visible.get());
                toggleButton.setText(visible.get() ? "▼" : "►");
                toggleButton.revalidate();
                toggleButton.repaint();
                isVisible.accept(visible.get());
            }
        });
        return toggleButton;
    }

    public static JScrollPane createContentScrollPanel(JPanel contentPanel) {
        return createContentScrollPanel(contentPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public static JScrollPane createContentScrollPanel(JPanel contentPanel, int vst, int hst) {
        JScrollPane settingsScrollableContainer = new JScrollPane(contentPanel, vst, hst);
        settingsScrollableContainer.setBorder(null);
        settingsScrollableContainer.getVerticalScrollBar().setUnitIncrement(8);
        return settingsScrollableContainer;
    }

}
