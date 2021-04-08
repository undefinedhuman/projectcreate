package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;

public class SettingsUI extends JFrame {

    public static SettingsUI instance;

    private JPanel settingsPanel;

    public SettingsUI() {
        if(instance == null)
            instance = this;
        setSize(480, 720);
        setResizable(false);
        setLocationRelativeTo(null);

        setContentPane(settingsPanel = new JPanel());
        settingsPanel.setBackground(new Color(60, 63, 65));
        settingsPanel.setLayout(null);
        settingsPanel.setSize(480, 720);
    }

    public void openConfig(Config config) {
        Tools.removeSettings(settingsPanel);
        Tools.addSettings(settingsPanel, config);
        setVisible(true);
    }

}
