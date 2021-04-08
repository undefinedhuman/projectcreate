package de.undefinedhuman.projectcreate.launcher.ui;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.launcher.game.GameAction;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

import javax.swing.*;
import java.util.ArrayList;

public class GameManagerUI extends JPanel {

    public static GameManagerUI instance;

    private DefaultComboBoxModel<Version> versionSelectionModel;
    private JComboBox<Version> versionSelection;
    private IconButton versionButton;
    private IconButton deleteButton;

    private GameAction currentAction;
    private Version selectedVersion = null;

    public GameManagerUI() {
        if(instance == null)
            instance = this;
        setLayout(null);
        setSize(1280, 100);

        initVersionSelection();
        initVersionButton();
        initDeleteButton();
        initSettingsButton();
    }

    public void init() {
        Version lastPlayed = LauncherConfig.instance.lastPlayedGameVersion.getVersion();
        if(InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.instance.gameInstallationPath.getFile(), lastPlayed) && versionSelectionModel.getIndexOf(lastPlayed) != -1)
            versionSelection.setSelectedItem(lastPlayed);
        else versionSelection.setSelectedIndex(0);
    }

    private void initVersionSelection() {
        versionSelectionModel = new DefaultComboBoxModel<>(InstallationUtils.fetchAvailableVersions(Launcher.DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection = new JComboBox<>(InstallationUtils.fetchAvailableVersions(Launcher.DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection.setBounds(25, getHeight()/2-16, 200, 32);
        versionSelection.setRenderer(new VersionCellRenderer());
        versionSelection.addActionListener(e -> {
            Version currentVersion = (Version) versionSelection.getSelectedItem();
            if(currentVersion == null)
                return;
            checkVersion(currentVersion);
        });
        add(versionSelection);
    }

    private void initVersionButton() {
        versionButton = new IconButton("download", 235, getHeight()/2-16, e -> {
            currentAction.onClick(selectedVersion);
        });
        add(versionButton);
    }

    private void initDeleteButton() {
        deleteButton = new IconButton("delete", 277, getHeight()/2-16, e -> {
            FsFile installationFile = new FsFile(LauncherConfig.instance.gameInstallationPath.getFile(), selectedVersion.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            ArrayList<String> errorMessages = FileError.checkFileForErrors(installationFile, FileError.NULL, FileError.NON_EXISTENT, FileError.NO_FILE);
            if(errorMessages.size() == 0 && installationFile.delete())
                Log.info("Successfully deleted game version " + selectedVersion.toString());
            checkVersion(selectedVersion);
        });
        deleteButton.setEnabled(false);
        add(deleteButton);
    }

    private void initSettingsButton() {
        IconButton settingsButton = new IconButton("settings", 319, getHeight() / 2 - 16, e -> {
            SettingsUI.instance.openConfig(
                    LauncherConfig.instance.gameInstallationPath,
                    LauncherConfig.instance.includeSnapshots,
                    LauncherConfig.instance.closeLauncherAfterGameStart,
                    LauncherConfig.instance.maximumMemory,
                    LauncherConfig.instance.initialMemory
            );
        });
        add(settingsButton);
    }

    public void checkVersion(Version version) {
        boolean isVersionDownloaded = InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.instance.gameInstallationPath.getFile(), version);
        deleteButton.setEnabled(isVersionDownloaded);
        versionButton.setIcon(isVersionDownloaded ? "play" : "download");
        currentAction = isVersionDownloaded ? GameAction.playAction() : GameAction.downloadAction();
        selectedVersion = version;
    }

}
