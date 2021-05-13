package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SettingsUI extends JFrame {

    private static SettingsUI instance;

    private static final int WINDOW_WIDTH = 480;
    private static final int WINDOW_HEIGHT = 720;

    public SettingsUI(ActionListener onSave, Setting<?>... settings) {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel settingsPanel;
        setContentPane(settingsPanel = new JPanel());
        settingsPanel.setBackground(new Color(60, 63, 65));
        settingsPanel.setLayout(null);
        settingsPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(
                WINDOW_WIDTH - Variables.OFFSET - Variables.CONTENT_WIDTH/4,
                WINDOW_HEIGHT - Variables.OFFSET - Variables.DEFAULT_CONTENT_HEIGHT,
                Variables.CONTENT_WIDTH/4,
                Variables.DEFAULT_CONTENT_HEIGHT);
        cancelButton.addActionListener(e -> {
            close();
        });
        settingsPanel.add(cancelButton);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(
                WINDOW_WIDTH - Variables.OFFSET*2 - Variables.CONTENT_WIDTH/4*2,
                WINDOW_HEIGHT - Variables.OFFSET - Variables.DEFAULT_CONTENT_HEIGHT,
                Variables.CONTENT_WIDTH/4,
                Variables.DEFAULT_CONTENT_HEIGHT);
        saveButton.addActionListener(e -> {
            onSave.actionPerformed(e);
            close();
        });
        settingsPanel.add(saveButton);

        Tools.addSettings(settingsPanel, 32, 32, 5, WINDOW_WIDTH - 64, Arrays.stream(settings));
        setVisible(true);
    }

    private void close() {
        setVisible(false);
        dispose();
        instance = null;
    }

    public static void openConfigUI(ActionListener onSave, Setting<?>... settings) {
        if(instance != null)
            instance.close();
        instance = new SettingsUI(onSave, settings);
    }

}
