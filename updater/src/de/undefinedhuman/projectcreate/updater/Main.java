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
    private static final String DOWNLOAD_DIRECTORY_URL = "http://alexanderpadberg.de/launcher/";

    private JProgressBar progressBar;
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
        setUndecorated(true);
        setBackground(new Color(1f, 1f, 1f, 0f));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(null);

        JLabel icon = new JLabel(new ImageIcon(Utils.scaleNearest(ResourceManager.loadImage("logo.png"), LOGO_SCALE)));
        icon.setBounds(0, 0, WINDOW_WIDTH, LOGO_SIZE.y);
        container.add(icon);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(0, LOGO_SIZE.y + 5, WINDOW_WIDTH, 10);
        container.add(progressBar);

        progressLabel = new JLabel("");
        progressLabel.setBounds(0, LOGO_SIZE.y + 20, WINDOW_WIDTH, 15);
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(progressLabel);

        setVisible(true);

        updateLauncher();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                close();
            }
        });
    }

    private void updateLauncher() {
        updateProgress("Checking for main dot directory...", 0);
        FsFile projectDotFile = new FsFile(Paths.GAME_PATH, Files.FileType.External);
        projectDotFile.mkdirs();
        if(!projectDotFile.exists())
            crash("Crash", "Root directory for the launcher cannot be generated. \nPlease restart, if the error persists, please contact the author.", false);
        updateProgress("Checking the installation directory...", 10);
        InstallationUtils.loadInstallationDirectory(config = new FsFile(projectDotFile, "launcher.config", Files.FileType.External), DEFAULT_INSTALLATION_PATH, installationPath, version);
        FsFile installationDirectory = new FsFile(installationPath.getString(), Files.FileType.Absolute);
        if(!installationDirectory.exists())
            crash("Crash", "Installation directory is nonexistent. \nPlease restart.", true);
        updateProgress("Checking for installed version...", 20);
        FsFile installationFile = new FsFile(installationDirectory, version.getString() + ".jar", Files.FileType.Absolute);
        ArrayList<Version> versions = InstallationUtils.fetchAvailableVersions(DOWNLOAD_DIRECTORY_URL);
        if(versions.isEmpty())
            crash("Crash", "Error while fetching available launcher versions. \nPlease restart, if the error persists, please contact the author.", true);

        Version maxVersion = Collections.max(versions);
        String downloadUrl = DOWNLOAD_DIRECTORY_URL + version.getString() + ".jar";
        if(!(installationFile.exists() && version.getVersion().equals(maxVersion) && DownloadUtils.fetchFileSize(downloadUrl) == installationFile.length())) {
            updateProgress("Download launcher version " + maxVersion.toString().substring(1) + "...", 30);
            FileUtils.deleteFile(installationFile);
            try {
                DownloadUtils.downloadFile(downloadUrl, installationFile);
            } catch (IOException | URISyntaxException e) {
                crash("Crash", "Error while downloading launcher version " + maxVersion.toString().substring(1) + "\nPlease restart, if the error persists, please contact the author.", true);
            }
        }

        updateProgress("Start launcher...", 90);
    }

    private void updateProgress(String message, int progress) {
        progressBar.setValue(progress);
        progressLabel.setText(message);
        revalidate();
        repaint();
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
    }
}
