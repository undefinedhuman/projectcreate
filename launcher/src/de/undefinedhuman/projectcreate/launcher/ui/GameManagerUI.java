package de.undefinedhuman.projectcreate.launcher.ui;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Stage;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.launcher.game.GameAction;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class GameManagerUI extends JPanel {

    private static final int WIDTH = Launcher.WINDOW_WIDTH/2;
    private static final int HEIGHT = 100;
    private static final Vector2i ICON_SIZE = new Vector2i(32, 32);

    private static volatile GameManagerUI instance;

    private DefaultComboBoxModel<Version> versionSelectionModel;
    private JComboBox<Version> versionSelection;
    private IconButton versionButton;
    private IconButton deleteButton;

    private GameAction currentAction;
    private Version selectedVersion = null;

    private VersionCellRenderer versionCellRenderer;

    private GameManagerUI() {
        setLayout(null);
        setBounds(0, Launcher.WINDOW_HEIGHT - HEIGHT, WIDTH, HEIGHT);

        initVersionSelection();
        initVersionButton();
        initDeleteButton();
        initSettingsButton();
    }

    public void init() {
        Version lastPlayed = LauncherConfig.getInstance().lastPlayedGameVersion.getValue();
        int selectedIndex = versionSelectionModel.getIndexOf(lastPlayed);
        if(selectedIndex == -1 || !InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getValue(), lastPlayed)) {
            selectedIndex = 0;
            for(int i = 0; i < versionSelectionModel.getSize(); i++) {
                if(!InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getValue(), versionSelectionModel.getElementAt(i)))
                    continue;
                selectedIndex = i;
                break;
            }
        }
        versionSelection.setSelectedItem(versionSelectionModel.getElementAt(selectedIndex));
    }

    private void initVersionSelection() {
        versionSelection = new JComboBox<>(versionSelectionModel = new DefaultComboBoxModel<>(getAvailableVersions()));
        versionSelection.setBounds(25, getHeight()/2-ICON_SIZE.y/2, 200, ICON_SIZE.y);
        versionCellRenderer = new VersionCellRenderer();
        versionSelection.setRenderer(versionCellRenderer);
        versionSelection.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Stream<Version> downloadedVersions = Arrays.stream(getAvailableVersions())
                        .filter(version -> InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getValue(), version));
                versionCellRenderer.setVersionDownloaded(downloadedVersions);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        versionSelection.addActionListener(e -> {
            Version currentVersion = (Version) versionSelection.getSelectedItem();
            if(currentVersion == null)
                return;
            checkVersion(currentVersion);
        });
        add(versionSelection);
    }

    private Version[] getAvailableVersions() {
        List<Version> availableVersion = InstallationUtils.fetchAvailableVersions(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().includeSnapshots.getValue() ? null : Stage.SNAPSHOT);
        Collections.reverse(availableVersion);
        return availableVersion.toArray(new Version[0]);
    }

    private void initVersionButton() {
        versionButton = new IconButton("download", 235, getHeight()/2-ICON_SIZE.y/2, ICON_SIZE, e -> {
            currentAction.onClick(selectedVersion);
        });
        add(versionButton);
    }

    private void initDeleteButton() {
        deleteButton = new IconButton("delete", 277, getHeight()/2-ICON_SIZE.y/2, ICON_SIZE, e -> {
            FsFile installationFile = new FsFile(LauncherConfig.getInstance().gameInstallationPath.getValue(), selectedVersion.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            ArrayList<String> errorMessages = FileError.checkFileForErrors(installationFile, FileError.NULL, FileError.NON_EXISTENT, FileError.NO_FILE);
            if(errorMessages.isEmpty() && installationFile.delete())
                Log.info("Successfully deleted game version " + selectedVersion.toString());
            checkVersion(selectedVersion);
        });
        deleteButton.setEnabled(false);
        add(deleteButton);
    }

    private void initSettingsButton() {
        IconButton settingsButton = new IconButton("settings", 319, getHeight()/2-ICON_SIZE.y/2, ICON_SIZE, e -> {
            SettingsUI.openConfigUI(
                    onClose -> {
                        LauncherConfig.getInstance().validate();
                        versionSelectionModel.removeAllElements();
                        versionSelectionModel = new DefaultComboBoxModel<>(getAvailableVersions());
                        versionSelection.setModel(versionSelectionModel);
                    },
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
        boolean isVersionDownloaded = InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getValue(), version);
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
