package de.undefinedhuman.projectcreate.updater;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.formdev.flatlaf.FlatDarkLaf;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationAdapter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.utils.manager.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.version.Version;
import de.undefinedhuman.projectcreate.updater.config.UpdaterConfig;
import de.undefinedhuman.projectcreate.updater.ui.UpdaterUI;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class Updater extends JFrame {

    public static final String DOWNLOAD_LAUNCHER_URL = DownloadUtils.SERVER_URL + "launcher/";
    public static final FsFile DEFAULT_INSTALLATION_DIRECTORY = new FsFile(Paths.GAME_PATH, "launcher/", Files.FileType.External);

    private static volatile Updater instance;

    private ManagerList managerList = new ManagerList();
    private UpdaterUI updaterUI;

    private Updater() {
        new HeadlessApplication(new HeadlessApplicationAdapter());
        FlatDarkLaf.install();
        managerList.addManager(Log.getInstance(), ConfigManager.getInstance().setConfigs(UpdaterConfig.getInstance()));

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
                managerList.delete();
                Gdx.app.exit();
                System.exit(0);
            }
        });
    }

    private void init() {
        Variables.NAME = "Updater";
        updaterUI.updateProgressText("Checking for project root directory...");
        InstallationUtils.checkProjectDotDirectory();
        managerList.init();
        Gdx.app.setApplicationLogger(Log.getInstance());
        Log.getInstance().setLogMessageDecorator(
                new LogMessage().andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT)).andThen(value -> LogMessageDecorators.withModuleName(value, "Game"))
        );
        Gdx.app.setLogLevel(Variables.LOG_LEVEL.ordinal());
    }

    public void updateLauncher() {
        updaterUI.updateProgressText("Checking the installation directory...");
        sleep();
        InstallationUtils.loadInstallationDirectory(UpdaterConfig.getInstance().firstRun.getValue(), UpdaterConfig.getInstance().installationPath, DEFAULT_INSTALLATION_DIRECTORY);
        updaterUI.updateProgressText("Checking for installed version...");
        sleep();
        FsFile currentlyInstalledVersion = new FsFile(UpdaterConfig.getInstance().installationPath.getValue().path(), UpdaterConfig.getInstance().version.getValue() + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
        List<Version> versions = InstallationUtils.fetchAvailableVersions(DOWNLOAD_LAUNCHER_URL, null);
        if(versions.isEmpty())
            Log.showErrorDialog("Error while fetching available launcher versions. \nPlease restart, if the error persists, please contact the author.", true);
        Version maxVersion = Collections.max(versions);
        String downloadUrl = DOWNLOAD_LAUNCHER_URL + maxVersion + DownloadUtils.DOWNLOAD_FILE_EXTENSION;
        if(!currentlyInstalledVersion.exists() || !UpdaterConfig.getInstance().version.getValue().equals(maxVersion) || DownloadUtils.fetchFileSize(downloadUrl) != currentlyInstalledVersion.length()) {
            updaterUI.updateProgressText("Download launcher version " + maxVersion + "...");
            sleep();
            FileUtils.deleteFile(currentlyInstalledVersion);
            Log.info(maxVersion);
            currentlyInstalledVersion = new FsFile(UpdaterConfig.getInstance().installationPath.getValue().path(), maxVersion + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
            try {
                DownloadUtils.downloadFile(downloadUrl, currentlyInstalledVersion);
            } catch (IOException | URISyntaxException e) {
                Log.showErrorDialog("Error while downloading launcher version " + maxVersion + "\nPlease restart, if the error persists, please contact the author.", true);
            }
        } else Log.info("Launcher already up to date. Version: " + maxVersion);

        UpdaterConfig.getInstance().version.setValue(maxVersion);

        updaterUI.updateProgressText("Start launcher...");
        sleep();
        try {
            Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/java" + (System.getProperty("os.name").contains("Windows") ? ".exe" : "") + " -jar " + currentlyInstalledVersion.path());
            setVisible(false);
            dispose();
        } catch (IOException ex) {
            Log.error("Error while starting launcher", ex);
        }
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
