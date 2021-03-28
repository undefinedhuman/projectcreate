package de.undefinedhuman.projectcreate.launcher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.formdev.flatlaf.FlatDarculaLaf;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main extends JFrame implements ApplicationListener {

    private static final String DOWNLOAD_GAME_URL = "http://alexanderpadberg.de/versions/";

    private FsFile gameVersionDir;
    private Container container;

    public Main() {
        new HeadlessApplication(this);
        new Log().init();
        FlatDarculaLaf.install();
        FsFile projectDotFile = new FsFile(Paths.GAME_PATH, Files.FileType.External);
        projectDotFile.mkdirs();
        if(!projectDotFile.exists())
            crash("Can't create the main launcher directory. \nPlease contact the author.");

        FsFile launcherConfig = new FsFile(projectDotFile, "launcher.config", Files.FileType.External);
        loadGameVersionDirectory(launcherConfig);

        setResizable(false);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        container = getContentPane();
        container.setBackground(new Color(60, 63, 65));
        container.setLayout(null);

        JComboBox<String> versionSelection = new JComboBox<>(getGameVersions().toArray(new String[0]));
        versionSelection.setBounds(100, 100, 100, 25);

        container.add(versionSelection);

        JButton installButton = new JButton("Install");
        installButton.setBounds(205, 100, 100, 25);
        installButton.addActionListener(e -> {
            try {
                if(versionSelection.getSelectedItem() == null)
                    return;
                DownloadFile.downloadFileWithResume(GAME_VERSION_URL + versionSelection.getSelectedItem(), new FsFile(gameVersionDir, versionSelection.getSelectedItem().toString(), Files.FileType.Absolute).path());
            } catch (IOException | URISyntaxException ex) {
                Log.instance.error("Error", "Error while downloading game version: " + versionSelection.getSelectedItem(), ex);
            }
        });
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

    private ArrayList<String> getGameVersions() {
        ArrayList<String> gameVersions = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(GAME_VERSION_URL).get();
        } catch (IOException ex) {
            Log.instance.error("Error", "Error connecting to versions server:", ex);
        }
        if(doc == null)
            return gameVersions;
        for (Element file : doc.select("pre a")) {
            String name = file.attr("href");
            if(!name.startsWith("v"))
                continue;
            gameVersions.add(name);
        }
        return gameVersions;
    }

    private void loadGameVersionDirectory(FsFile launcherConfig) {
        if(!launcherConfig.exists())
            gameVersionDir = chooseGameVersionDir();
        else {
            FileReader reader = launcherConfig.getFileReader(false);
            if(reader.nextLine() != null) {
                String[] data = reader.getNextString().split("=", 2);
                if(data.length != 2 || !data[0].equalsIgnoreCase("gameVersionsDirectoryPath") || !(gameVersionDir = new FsFile(data[1], Files.FileType.Absolute)).exists())
                    gameVersionDir = chooseGameVersionDir();
            } else gameVersionDir = chooseGameVersionDir();
            reader.close();
        }
    }

    private FsFile chooseGameVersionDir() {
        JFileChooser chooser = new JFileChooser();
        FsFile versionsDirectory = new FsFile(".projectcreate/versions/", Files.FileType.External);
        versionsDirectory.mkdirs();
        if(!versionsDirectory.exists())
            JOptionPane.showMessageDialog(null, "Can't create default versions folder in launcher directory! Choose a different directory to install game versions!", "Error!", JOptionPane.ERROR_MESSAGE);
        else chooser.setCurrentDirectory(versionsDirectory.file());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            crash("Choose a writable folder to install game versions! \nPlease restart the launcher!");
        if(!chooser.getSelectedFile().equals(versionsDirectory.file()) && versionsDirectory.delete())
            Log.info("Deleted temporary versions folder!");
        return new FsFile(chooser.getSelectedFile().getAbsolutePath(), Files.FileType.Absolute);
    }

    private void crash(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Crash", JOptionPane.ERROR_MESSAGE);
        Log.instance.exit(errorMessage);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void create() {}

    @Override
    public void render() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

}
