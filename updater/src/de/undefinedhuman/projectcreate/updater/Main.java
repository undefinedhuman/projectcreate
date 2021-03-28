package de.undefinedhuman.projectcreate.updater;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.formdev.flatlaf.FlatDarkLaf;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;
import de.undefinedhuman.projectcreate.updater.window.HeadlessApplicationListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends JFrame {

    public static Main instance;

    private static final int LOGO_SCALE = 3;
    private static final Vector2i LOGO_SIZE = new Vector2i(128, 32).mul(LOGO_SCALE);
    private static final int WINDOW_WIDTH = LOGO_SIZE.x;
    private static final String DEFAULT_INSTALLATION_PATH = Paths.GAME_PATH + "launcher/";
    private static final String DOWNLOAD_LAUNCHER_URL = "http://alexanderpadberg.de/launcher/";

    private JLabel progressLabel;

    private Setting installationPath = new Setting(SettingType.String, "installationPath", new FsFile(DEFAULT_INSTALLATION_PATH, Files.FileType.External).path()),
                    version = new Setting(SettingType.Version, "version", new Version(0, 0, 0).toString());
    private FsFile config;

    public Main() {
        if(instance == null)
            instance = this;
        new HeadlessApplication(new HeadlessApplicationListener());
        init();
        FlatDarkLaf.install();

        setResizable(false);
        setSize(LOGO_SIZE.x, LOGO_SIZE.y + 40);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(60, 63, 65));
        setContentPane(container);

        JLabel icon = new JLabel(new ImageIcon(Utils.scaleNearest(ResourceManager.loadImage("logo.png"), LOGO_SCALE)));
        icon.setBounds(0, 0, WINDOW_WIDTH, LOGO_SIZE.y);
        container.add(icon);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(0, LOGO_SIZE.y + 5, WINDOW_WIDTH, 10);
        progressBar.setIndeterminate(true);
        container.add(progressBar);

        progressLabel = new JLabel("");
        progressLabel.setBounds(0, LOGO_SIZE.y + 20, WINDOW_WIDTH, 15);
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(progressLabel);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                close();
            }
        });
    }

    public void updateLauncher() {
        updateProgress("Checking for main dot directory...");
        FsFile projectDotFile = new FsFile(Paths.GAME_PATH, Files.FileType.External);
        projectDotFile.mkdirs();
        if(!projectDotFile.exists())
            crash("Crash", "Root directory for the launcher cannot be generated. \nPlease restart, if the error persists, please contact the author.", false);
        updateProgress("Checking the installation directory...");
        InstallationUtils.loadInstallationDirectory(config = new FsFile(projectDotFile, "launcher.config", Files.FileType.External), DEFAULT_INSTALLATION_PATH, installationPath, version);
        FsFile installationDirectory = new FsFile(installationPath.getString(), Files.FileType.Absolute);
        if(!installationDirectory.exists())
            crash("Crash", "Installation directory is nonexistent. \nPlease restart.", true);
        updateProgress("Checking for installed version...");
        FsFile installationFile = new FsFile(installationDirectory, version.getString() + ".jar", Files.FileType.Absolute);
        ArrayList<Version> versions = InstallationUtils.fetchAvailableVersions(DOWNLOAD_LAUNCHER_URL);
        if(versions.isEmpty())
            crash("Crash", "Error while fetching available launcher versions. \nPlease restart, if the error persists, please contact the author.", true);

        Version maxVersion = Collections.max(versions);
        String loggableVersion = maxVersion.toString().substring(1);
        String downloadUrl = DOWNLOAD_LAUNCHER_URL + maxVersion.toString() + ".jar";
        if(!(installationFile.exists() && version.getVersion().equals(maxVersion) && DownloadUtils.fetchFileSize(downloadUrl) == installationFile.length())) {
            updateProgress("Download launcher version " + loggableVersion + "...");
            Log.info("Downloading newest launcher version " + loggableVersion);
            FileUtils.deleteFile(installationFile);
            installationFile = new FsFile(installationDirectory, maxVersion.toString() + ".jar", Files.FileType.Absolute);
            try {
                DownloadUtils.downloadFile(downloadUrl, installationFile);
            } catch (IOException | URISyntaxException e) {
                crash("Crash", "Error while downloading launcher version " + loggableVersion + "\nPlease restart, if the error persists, please contact the author.", true);
            }
        } else Log.info("Launcher already up to date. Version: " + loggableVersion);

        version.setValue(maxVersion.toString());

        updateProgress("Start launcher...");
        try {
            Runtime.getRuntime().exec("java -jar " + installationFile.path());
            setVisible(false);
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgress(String message) {
        progressLabel.setText(message);
    }

    private void init() {
        Variables.NAME = "Updater";
        new Log().init();
        Gdx.app.setApplicationLogger(Log.instance);
        Gdx.app.setLogLevel(Variables.LOG_LEVEL);
    }

    public void saveConfig() {
        FileWriter writer = config.getFileWriter(true);
        Utils.saveSettings(writer, installationPath, version);
        writer.close();
    }

    public static void close() {
        Main.instance.saveConfig();
        Gdx.app.exit();
        Log.instance.exit();
    }

    public static void crash(String title, String errorMessage, boolean close) {
        Log.instance.error(title, errorMessage);
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.ERROR_MESSAGE);
        if(close)
            Main.close();
    }

    public static void main(String[] args) {
        new Main();
        Main.instance.updateLauncher();
    }
}
