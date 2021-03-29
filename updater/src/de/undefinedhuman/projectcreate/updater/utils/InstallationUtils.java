package de.undefinedhuman.projectcreate.updater.utils;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.updater.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class InstallationUtils {

    public static void loadInstallationDirectory(FsFile config, String defaultInstallationPath, Setting installationPath, Setting version) {
        if(!config.exists())
            installationPath.setValue(chooseInstallationDirectory(defaultInstallationPath));
        else {
            FileReader reader = config.getFileReader(true);
            Tools.loadSettings(reader, installationPath, version);
            if(!new FsFile(installationPath.getString(), Files.FileType.Absolute).exists())
                installationPath.setValue(chooseInstallationDirectory(defaultInstallationPath));
            reader.close();
        }
    }

    public static String chooseInstallationDirectory(String defaultInstallationDirPath) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a installation directory...");
        FsFile defaultInstallationDirectory = new FsFile(defaultInstallationDirPath, Files.FileType.External);
        defaultInstallationDirectory.mkdirs();
        if(!defaultInstallationDirectory.exists())
            JOptionPane.showMessageDialog(null, "Can't create default installation folder! \n Choose a different writable installation directory!", "Error", JOptionPane.ERROR_MESSAGE);
        else chooser.setCurrentDirectory(defaultInstallationDirectory.file());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            Main.crash("Crash", "Choose a writable installation directory! \nPlease restart!", true);
        if(!chooser.getSelectedFile().equals(defaultInstallationDirectory.file()) && defaultInstallationDirectory.delete())
            Log.info("Deleted default installation folder!");
        return chooser.getSelectedFile().getAbsolutePath();
    }

    public static ArrayList<Version> fetchAvailableVersions(String url) {
        ArrayList<Version> availableVersions = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Log.instance.error("Error", "Error connecting to versions server:", ex);
        }
        if(doc == null)
            return availableVersions;
        for (Element file : doc.select("pre a")) {
            String fileName = file.attr("href");
            if(!fileName.startsWith("v"))
                continue;
            availableVersions.add(Version.parse(fileName.split("\\.jar")[0]));
        }
        return availableVersions;
    }

}
