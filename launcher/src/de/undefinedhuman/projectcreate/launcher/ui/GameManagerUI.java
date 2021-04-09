package de.undefinedhuman.projectcreate.launcher.ui;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.launcher.game.GameAction;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

import javax.swing.*;
import java.util.ArrayList;

public class GameManagerUI extends JPanel {

    private static final Vector2i ICON_SIZE = new Vector2i(32, 32);

    private static volatile GameManagerUI instance;

    private DefaultComboBoxModel<Version> versionSelectionModel;
    private JComboBox<Version> versionSelection;
    private IconButton versionButton;
    private IconButton deleteButton;

    private GameAction currentAction;
    private Version selectedVersion = null;

    private GameManagerUI() {
        setLayout(null);
        setBounds(0, 620, 640, 100);

        initVersionSelection();
        initVersionButton();
        initDeleteButton();
        initSettingsButton();
    }

    public void init() {
        Version lastPlayed = LauncherConfig.getInstance().lastPlayedGameVersion.getVersion();
        int selectedIndex = versionSelectionModel.getIndexOf(lastPlayed);
        if(selectedIndex == -1 || !InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getFile(), lastPlayed)) {
            selectedIndex = 0;
            for(int i = 0; i < versionSelectionModel.getSize(); i++) {
                Version currentVersion = versionSelectionModel.getElementAt(i);
                if(InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getFile(), currentVersion)
                        && currentVersion.compareTo(versionSelectionModel.getElementAt(selectedIndex)) > 0)
                    selectedIndex = i;
            }
        }
        versionSelection.setSelectedItem(versionSelectionModel.getElementAt(selectedIndex));
    }

    private void initVersionSelection() {
        versionSelectionModel = new DefaultComboBoxModel<>(InstallationUtils.fetchAvailableVersions(Launcher.DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection = new JComboBox<>(InstallationUtils.fetchAvailableVersions(Launcher.DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection.setBounds(25, getHeight()/2-ICON_SIZE.y/2, 200, ICON_SIZE.y);
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
        versionButton = new IconButton("download", 235, getHeight()/2-ICON_SIZE.y/2, e -> {
            currentAction.onClick(selectedVersion);
        });
        add(versionButton);
    }

    private void initDeleteButton() {
        deleteButton = new IconButton("delete", 277, getHeight()/2-ICON_SIZE.y/2, e -> {
            FsFile installationFile = new FsFile(LauncherConfig.getInstance().gameInstallationPath.getFile(), selectedVersion.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            ArrayList<String> errorMessages = FileError.checkFileForErrors(installationFile, FileError.NULL, FileError.NON_EXISTENT, FileError.NO_FILE);
            if(errorMessages.size() == 0 && installationFile.delete())
                Log.info("Successfully deleted game version " + selectedVersion.toString());
            checkVersion(selectedVersion);
        });
        deleteButton.setEnabled(false);
        add(deleteButton);
    }

    private void initSettingsButton() {
        IconButton settingsButton = new IconButton("settings", 319, getHeight()/2-ICON_SIZE.y/2, e -> {
            SettingsUI.getInstance().openConfig(
                    LauncherConfig.getInstance().gameInstallationPath,
                    LauncherConfig.getInstance().includeSnapshots,
                    LauncherConfig.getInstance().closeLauncherAfterGameStart,
                    LauncherConfig.getInstance().maximumMemory,
                    LauncherConfig.getInstance().initialMemory
            );
        });
        add(settingsButton);
    }

    public void checkVersion(Version version) {
        boolean isVersionDownloaded = InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getFile(), version);
        deleteButton.setEnabled(isVersionDownloaded);
        versionButton.setIcon(isVersionDownloaded ? "play" : "download");
        currentAction = isVersionDownloaded ? GameAction.playAction() : GameAction.downloadAction();
        selectedVersion = version;
    }

    public static GameManagerUI getInstance() {
        if (instance == null) {
            synchronized (GameManagerUI.class) {
                if (instance == null)
                    instance = new GameManagerUI();
            }
        }
        return instance;
    }

}
