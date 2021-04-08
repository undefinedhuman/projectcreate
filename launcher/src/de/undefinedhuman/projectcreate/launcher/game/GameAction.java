package de.undefinedhuman.projectcreate.launcher.game;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
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
            FsFile gameVersion = new FsFile(LauncherConfig.instance.gameInstallationPath.getFile(), version.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            if(!InstallationUtils.isVersionDownloaded(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.instance.gameInstallationPath.getFile(), version)) {
                GameManagerUI.instance.checkVersion(version);
                return;
            }
            try {
                float xmx = LauncherConfig.instance.maximumMemory.getFloat();
                float xms = LauncherConfig.instance.initialMemory.getFloat();
                String javaCommand =
                        "java -jar " +
                        gameVersion.path() +
                        (xmx != 0 ? String.format(" -Xmx%.1fg", xmx) : "") +
                        (xms != 0 ? String.format(" -Xms%.1fg", xms) : "");
                Runtime.getRuntime().exec(javaCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LauncherConfig.instance.lastPlayedGameVersion.setValue(version.toString());
            if(LauncherConfig.instance.closeLauncherAfterGameStart.getBoolean()) {
                Launcher.instance.setVisible(false);
                Launcher.instance.dispose();
            }
        };
    }

    static GameAction downloadAction() {
        return version -> {
            while(!InstallationUtils.hasSufficientSpace(Launcher.DOWNLOAD_GAME_URL, LauncherConfig.instance.gameInstallationPath.getFile(), version)) {
                Log.crash("Error", "Installation directory does not have enough available space.\nPlease choose another directory.", false);
                LauncherConfig.instance.gameInstallationPath.setValue(InstallationUtils.chooseInstallationDirectory(Launcher.DEFAULT_INSTALLATION_DIRECTORY));
            }
            String downloadURL = Launcher.DOWNLOAD_GAME_URL + version + DownloadUtils.DOWNLOAD_FILE_EXTENSION;
            FsFile destination = new FsFile(LauncherConfig.instance.gameInstallationPath.getFile(), version.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            long downloadedBytes = 0;
            try {
                downloadedBytes = DownloadUtils.downloadFile(downloadURL, destination);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            if(downloadedBytes == DownloadUtils.fetchFileSize(downloadURL))
                GameManagerUI.instance.checkVersion(version);
        };
    }

}
