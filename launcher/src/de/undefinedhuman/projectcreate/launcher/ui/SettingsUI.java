package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SettingsUI extends JFrame {

    public static SettingsUI instance;

    private JPanel settingsPanel;

    public SettingsUI() {
        if(instance == null)
            instance = this;
        setSize(480, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setContentPane(settingsPanel = new JPanel());
        settingsPanel.setBackground(new Color(60, 63, 65));
        settingsPanel.setLayout(null);
        settingsPanel.setSize(480, 720);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                LauncherConfig.instance.validate();
            }
        });

    }

    public void openConfig(Setting... settings) {
        Tools.removeSettings(settingsPanel);
        Tools.addSettings(settingsPanel, 32, 32, 5, settings);
        setVisible(true);
    }

}
