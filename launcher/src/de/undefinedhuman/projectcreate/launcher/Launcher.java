package de.undefinedhuman.projectcreate.launcher;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.formdev.flatlaf.FlatDarculaLaf;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.launcher.icon.IconManager;
import de.undefinedhuman.projectcreate.launcher.ui.GameManagerUI;
import de.undefinedhuman.projectcreate.launcher.ui.IconButton;
import de.undefinedhuman.projectcreate.launcher.ui.SettingsUI;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;
import de.undefinedhuman.projectcreate.updater.window.HeadlessApplicationListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Launcher extends JFrame {

    public static final String DOWNLOAD_GAME_URL = DownloadUtils.SERVER_URL + "versions/";
    public static final FsFile DEFAULT_INSTALLATION_DIRECTORY = new FsFile(Paths.GAME_PATH, "versions/", Files.FileType.External);

    public static Launcher instance;

    private ManagerList managerList = new ManagerList();

    public Launcher() {
        if(instance == null)
            instance = this;
        new HeadlessApplication(new HeadlessApplicationListener());
        setUIComponentProperties();
        FlatDarculaLaf.install();
        managerList.addManager(new Log() {
            @Override
            public void close() {
                Launcher.this.close();
            }
        }, new ConfigManager(new LauncherConfig()), new IconManager());

        setResizable(false);
        setUndecorated(false);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        new SettingsUI();

        Container container = getContentPane();
        container.setBackground(new Color(60, 63, 65));
        container.setLayout(null);

        JPanel gameManagerPanel = new JPanel();
        gameManagerPanel.setBounds(0, 720 - 100, 1280, 100);
        gameManagerPanel.setLayout(null);

        GameManagerUI gameManagerUI = new GameManagerUI();
        gameManagerUI.setLocation(0, 620);
        container.add(gameManagerUI);

        JComboBox<Version> versionSelection = new JComboBox<>(InstallationUtils.fetchAvailableVersions(DOWNLOAD_GAME_URL).toArray(new Version[0]));
        versionSelection.setBounds(25, 34, 200, 32);
        gameManagerPanel.setBackground(Color.RED);
        gameManagerPanel.add(versionSelection);

        //container.add(new IconButton("exit", 1239, 9, e -> Main.instance.close()), );

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(100, 100, 720, 480);
        tabbedPane.addTab("Main", new JPanel());
        tabbedPane.addTab("About", new JPanel());
        tabbedPane.addTab("Patch Notes", new JPanel());
        //container.add(tabbedPane, BorderLayout.EAST);

        gameManagerPanel.add(new IconButton("download", 240, 34, e -> {
            /*try {
                if(versionSelection.getSelectedItem() == null)
                    return;
                DownloadUtils.downloadFile(DOWNLOAD_GAME_URL + versionSelection.getSelectedItem(), new FsFile(gameVersionDir, versionSelection.getSelectedItem().toString(), Files.FileType.Absolute));
            } catch (IOException | URISyntaxException ex) {
                Log.instance.error("Error", "Error while downloading game version: " + versionSelection.getSelectedItem(), ex);
            }*/
        }));

        gameManagerPanel.add(new IconButton("delete", 290, 34, e -> {}));

        JButton settingsButton = new IconButton("settings", 350, 34, e -> {});
        settingsButton.setEnabled(false);
        gameManagerPanel.add(settingsButton);

        JLabel icon = new JLabel(new ImageIcon(ResourceManager.loadImage("logo/288x96.png")));
        icon.setBounds(1280/4/2 - 144, 32, 288, 96);
        add(icon);

        //container.add(gameManagerPanel, BorderLayout.SOUTH);
        container.setPreferredSize(new Dimension(1280, 720));
        pack();

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                close();
            }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        Variables.NAME = "Launcher";
        InstallationUtils.checkProjectDotDirectory();
        managerList.init();
        setGDXLog();
        GameManagerUI.instance.init();
    }

    private void setGDXLog() {
        Gdx.app.setApplicationLogger(Log.instance);
        Gdx.app.setLogLevel(Variables.LOG_LEVEL);
    }

    private void setUIComponentProperties() {
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 0);
        UIManager.put("CheckBox.arc", 0);
        UIManager.put("ProgressBar.arc", 0);

        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Component.focusWidth", 1);
    }

    public void close() {
        managerList.delete();
        Gdx.app.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        new Launcher();
        Launcher.instance.init();
    }

}
