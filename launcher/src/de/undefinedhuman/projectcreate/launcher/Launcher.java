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
import de.undefinedhuman.projectcreate.launcher.config.LauncherConfig;
import de.undefinedhuman.projectcreate.launcher.icon.IconManager;
import de.undefinedhuman.projectcreate.launcher.ui.GameManagerUI;
import de.undefinedhuman.projectcreate.launcher.ui.SettingsUI;
import de.undefinedhuman.projectcreate.updater.utils.DownloadUtils;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;
import de.undefinedhuman.projectcreate.updater.window.HeadlessApplicationListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Launcher extends JFrame {

    public static final String DOWNLOAD_GAME_URL = DownloadUtils.SERVER_URL + "game/";
    public static final FsFile DEFAULT_INSTALLATION_DIRECTORY = new FsFile(Paths.GAME_PATH, "game/", Files.FileType.External);

    private static volatile Launcher instance;

    private ManagerList managerList = new ManagerList();

    private Launcher() {
        new HeadlessApplication(new HeadlessApplicationListener());
        FlatDarculaLaf.install();
        setUIComponentProperties();
        managerList.addManager(Log.getInstance(), ConfigManager.getInstance().setConfigs(LauncherConfig.getInstance()), IconManager.getInstance());

        setResizable(false);
        setUndecorated(false);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        Container container = getContentPane();
        container.setBackground(new Color(60, 63, 65));
        container.setLayout(null);

        container.add(GameManagerUI.getInstance());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(1280/4, 32, 1280/4*3, 588);
        tabbedPane.addTab("Main", new JPanel());
        tabbedPane.addTab("Patch Notes", new JPanel());
        tabbedPane.addTab("Roadmap", new JPanel());
        tabbedPane.addTab("About", new JPanel());
        container.add(tabbedPane);

        JLabel icon = new JLabel(new ImageIcon(ResourceManager.loadImage("logo/288x96.png")));
        icon.setBounds(1280/4/2 - 144, 32, 288, 96);
        container.add(icon);

        container.setPreferredSize(new Dimension(1280, 720));
        pack();

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                managerList.delete();
                Gdx.app.exit();
                System.exit(0);
            }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        Variables.NAME = "Launcher";
        InstallationUtils.checkProjectDotDirectory();
        managerList.init();
        setGDXLog();
        new SettingsUI();
        GameManagerUI.getInstance().init();
    }

    private void setGDXLog() {
        Gdx.app.setApplicationLogger(Log.getInstance());
        Gdx.app.setLogLevel(Variables.LOG_LEVEL.ordinal());
    }

    private void setUIComponentProperties() {
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 0);
        UIManager.put("CheckBox.arc", 0);
        UIManager.put("ProgressBar.arc", 0);

        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Component.focusWidth", 1);
    }

    public static Launcher getInstance() {
        if (instance == null) {
            synchronized (Launcher.class) {
                if (instance == null)
                    instance = new Launcher();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Launcher.getInstance();
        Launcher.getInstance().init();
    }

}
