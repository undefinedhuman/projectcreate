package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

import javax.swing.*;
import java.awt.*;

public class GameManagerUI extends JPanel {

    private JComboBox<Version> versionSelection;
    private IconButton versionButton, deleteButton, settingsButton;

    public GameManagerUI() {
        setLayout(null);
        setBackground(Color.RED);
        setSize(1280, 100);

        initVersionSelection();
        initVersionButton();
        initDeleteButton();
        initSettingsButton();
    }

    private void initVersionSelection() {
        versionSelection = new JComboBox<>(InstallationUtils.fetchAvailableVersions(Launcher.DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection.setBounds(25, getHeight()/2-16, 200, 32);
        versionSelection.setRenderer(new VersionCellRenderer());
        versionSelection.addActionListener(e -> {
            Version currentVersion = (Version) versionSelection.getSelectedItem();
            if(currentVersion == null)
                return;
        });
        add(versionSelection);
    }

    private void initVersionButton() {
        versionButton = new IconButton("download", 235, getHeight()/2-16, e -> {

        });
        add(versionButton);
    }

    private void initDeleteButton() {
        deleteButton = new IconButton("delete", 277, getHeight()/2-16, e -> downloadAction());
        add(deleteButton);
    }

    private void initSettingsButton() {
        settingsButton = new IconButton("settings", 319, getHeight()/2-16, e -> {});
        add(settingsButton);
    }

    private void checkVersion(Version version) {

    }

    private void downloadAction() {
        while(!InstallationUtils.checkForSufficientSpace(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.instance.gameInstallationPath.getFile())) {
            Log.crash("Error", "Installation directory does not have enough available space.\nPlease choose another directory.", false);
            LauncherConfig.instance.gameInstallationPath.setValue(InstallationUtils.chooseInstallationDirectory(Launcher.DEFAULT_INSTALLATION_DIRECTORY));
        }
    }

}
