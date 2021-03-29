package de.undefinedhuman.projectcreate.launcher;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;
import de.undefinedhuman.projectcreate.updater.window.HeadlessApplicationListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    public static Main instance;

    private static final String DOWNLOAD_GAME_URL = "http://alexanderpadberg.de/versions/";

    private FsFile gameVersionDir;
    private Container container;

    public Main() {
        if(instance == null)
            instance = this;
        new HeadlessApplication(new HeadlessApplicationListener());
        new Log().init();
        styleUI();
        FlatDarculaLaf.install();
        FsFile projectDotFile = new FsFile(Paths.GAME_PATH, Files.FileType.External);
        projectDotFile.mkdirs();
        if(!projectDotFile.exists())
            crash("Can't create the main launcher directory. \nPlease contact the author.");

        FsFile launcherConfig = new FsFile(projectDotFile, "launcher.config", Files.FileType.External);
        //loadGameVersionDirectory(launcherConfig);

        setResizable(false);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        container = getContentPane();
        container.setBackground(new Color(60, 63, 65));
        container.setLayout(null);

        JComboBox<Version> versionSelection = new JComboBox<>(InstallationUtils.fetchAvailableVersions(DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection.setBounds(100, 100, 100, 25);
        container.add(versionSelection);

        FlatSVGIcon icon = new FlatSVGIcon("icon/download.svg");
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, 32, 32);
        container.add(label);

        JButton installButton = new JButton("Install");
        installButton.setBounds(205, 100, 100, 25);
        installButton.setBackground(Color.GREEN);
        installButton.setForeground(Color.WHITE);
        /*installButton.addActionListener(e -> {
            try {
                if(versionSelection.getSelectedItem() == null)
                    return;
                DownloadFile.downloadFileWithResume(GAME_VERSION_URL + versionSelection.getSelectedItem(), new FsFile(gameVersionDir, versionSelection.getSelectedItem().toString(), Files.FileType.Absolute).path());
            } catch (IOException | URISyntaxException ex) {
                Log.instance.error("Error", "Error while downloading game version: " + versionSelection.getSelectedItem(), ex);
            }
        });*/
        container.add(installButton);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if(gameVersionDir != null && gameVersionDir.exists()) {
                    FileWriter writer = launcherConfig.getFileWriter(false);
                    writer.writeString("gameVersionsDirectoryPath=" + gameVersionDir.file().getAbsolutePath());
                    writer.close();
                }
                Gdx.app.exit();
                Log.instance.exit();
            }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void crash(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Crash", JOptionPane.ERROR_MESSAGE);
        Log.instance.exit(errorMessage);
    }

    private void init() {
        Variables.NAME = "Updater";
        new Log().init();
        Gdx.app.setApplicationLogger(Log.instance);
        Gdx.app.setLogLevel(Variables.LOG_LEVEL);
    }

    public void styleUI() {
        UIManager.put( "Button.arc", 0 );
        UIManager.put( "Component.arc", 0 );
        UIManager.put( "CheckBox.arc", 0 );
        UIManager.put( "ProgressBar.arc", 0 );

        UIManager.put( "Component.arrowType", "chevron" );
        UIManager.put( "Component.focusWidth", 1 );
    }

    public void saveConfig() {
        /*FileWriter writer = config.getFileWriter(true);
        Utils.saveSettings(writer, installationPath, version);
        writer.close();*/
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
