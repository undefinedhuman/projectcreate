package de.undefinedhuman.projectcreate.updater;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.formdev.flatlaf.FlatDarkLaf;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.updater.config.UpdaterConfig;
import de.undefinedhuman.projectcreate.updater.ui.UpdaterUI;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;
import de.undefinedhuman.projectcreate.updater.window.HeadlessApplicationListener;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class Updater extends JFrame {

    public static final String DOWNLOAD_LAUNCHER_URL = DownloadUtils.SERVER_URL + "launcher/";
    public static final FsFile DEFAULT_INSTALLATION_DIRECTORY = new FsFile(Paths.GAME_PATH, "launcher/", Files.FileType.External);

    private static volatile Updater instance;

    private ManagerList managerList = new ManagerList();
    private UpdaterUI updaterUI;

    private Updater() {
        new HeadlessApplication(new HeadlessApplicationListener());
        FlatDarkLaf.install();
        managerList.addManager(new Log() {
            @Override
            public void close() {
                Updater.this.close();
            }
        }, ConfigManager.getInstance().setConfigs(UpdaterConfig.getInstance()));

        setContentPane(updaterUI = new UpdaterUI());

        setResizable(false);
        setSize(updaterUI.getWindowSize());
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                close();
            }
        });
    }

    private void init() {
        Variables.NAME = "Updater";
        updaterUI.updateProgressText("Checking for project root directory...");
        InstallationUtils.checkProjectDotDirectory();
        managerList.init();
        Gdx.app.setApplicationLogger(Log.instance);
        Gdx.app.setLogLevel(Variables.LOG_LEVEL);
    }

    public void updateLauncher() {
        updaterUI.updateProgressText("Checking the installation directory...");
        sleep();
        InstallationUtils.loadInstallationDirectory(UpdaterConfig.getInstance().firstRun.getBoolean(), UpdaterConfig.getInstance().installationPath, DEFAULT_INSTALLATION_DIRECTORY);
        updaterUI.updateProgressText("Checking for installed version...");
        sleep();
        FsFile currentlyInstalledVersion = new FsFile(UpdaterConfig.getInstance().installationPath.getFile(), UpdaterConfig.getInstance().version.getString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
        ArrayList<Version> versions = InstallationUtils.fetchAvailableVersions(DOWNLOAD_LAUNCHER_URL);
        if(versions.isEmpty())
            Log.crash("Crash", "Error while fetching available launcher versions. \nPlease restart, if the error persists, please contact the author.", true);
        Version maxVersion = Collections.max(versions);
        String loggableVersion = maxVersion.toString().substring(1);
        String downloadUrl = DOWNLOAD_LAUNCHER_URL + maxVersion + DownloadUtils.DOWNLOAD_FILE_EXTENSION;
        if(!currentlyInstalledVersion.exists() || !UpdaterConfig.getInstance().version.getVersion().equals(maxVersion) || DownloadUtils.fetchFileSize(downloadUrl) != currentlyInstalledVersion.length()) {
            updaterUI.updateProgressText("Download launcher version " + loggableVersion + "...");
            sleep();
            Log.info("Downloading newest launcher version " + loggableVersion);
            FileUtils.deleteFile(currentlyInstalledVersion);
            currentlyInstalledVersion = new FsFile(UpdaterConfig.getInstance().installationPath.getFile(), maxVersion + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            try {
                DownloadUtils.downloadFile(downloadUrl, currentlyInstalledVersion);
            } catch (IOException | URISyntaxException e) {
                Log.crash("Crash", "Error while downloading launcher version " + loggableVersion + "\nPlease restart, if the error persists, please contact the author.", true);
            }
        } else Log.info("Launcher already up to date. Version: " + loggableVersion);

        UpdaterConfig.getInstance().version.setValue(maxVersion.toString());

        updaterUI.updateProgressText("Start launcher...");
        sleep();
        try {
            Runtime.getRuntime().exec("java -jar " + currentlyInstalledVersion.path());
            setVisible(false);
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        managerList.delete();
        Gdx.app.exit();
        System.exit(0);
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Updater getInstance() {
        if (instance == null) {
            synchronized (Updater.class) {
                if (instance == null)
                    instance = new Updater();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Updater.getInstance();
        Updater.getInstance().init();
        Updater.getInstance().updateLauncher();
    }

}
