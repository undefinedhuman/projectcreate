package de.undefinedhuman.projectcreate.launcher.game;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.launcher.ui.GameManagerUI;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public interface GameAction {
    void onClick(Version version);

    static GameAction playAction() {
        return version -> {
            FsFile gameVersion = new FsFile(LauncherConfig.getInstance().gameInstallationPath.getFile(), version.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            if(!InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getFile(), version)) {
                GameManagerUI.getInstance().checkVersion(version);
                return;
            }
            try {
                float xmx = LauncherConfig.getInstance().maximumMemory.getFloat();
                float xms = LauncherConfig.getInstance().initialMemory.getFloat();
                String javaCommand =
                        System.getProperty("java.home") + "/bin/java" + (System.getProperty("os.name").contains("Windows") ? ".exe" : "") + " -jar " +
                        gameVersion.path() +
                        (xmx != 0 ? String.format(" -Xmx%.1fg", xmx) : "") +
                        (xms != 0 ? String.format(" -Xms%.1fg", xms) : "");
                Runtime.getRuntime().exec(javaCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LauncherConfig.getInstance().lastPlayedGameVersion.setValue(version.toString());
            if(LauncherConfig.getInstance().closeLauncherAfterGameStart.getBoolean()) {
                Launcher.getInstance().setVisible(false);
                Launcher.getInstance().dispose();
            }
        };
    }

    static GameAction downloadAction() {
        return version -> {
            while(!InstallationUtils.hasSufficientSpace(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.getInstance().gameInstallationPath.getFile(), version)) {
                Log.getInstance().showErrorDialog(Level.ERROR, "Installation directory does not have enough available space.\nPlease choose another directory.", false);
                LauncherConfig.getInstance().gameInstallationPath.setValue(InstallationUtils.chooseInstallationDirectory(Launcher.DEFAULT_INSTALLATION_DIRECTORY));
            }
            String downloadURL = Launcher.DOWNLOAD_GAME_URL + version + DownloadUtils.DOWNLOAD_FILE_EXTENSION;
            FsFile destination = new FsFile(LauncherConfig.getInstance().gameInstallationPath.getFile(), version.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            long downloadedBytes = 0;
            try {
                downloadedBytes = DownloadUtils.downloadFile(downloadURL, destination);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            if(downloadedBytes == DownloadUtils.fetchFileSize(downloadURL)) {
                Log.info("Successfully downloaded game version: " + version);
                GameManagerUI.getInstance().checkVersion(version);
            }
        };
    }

}
