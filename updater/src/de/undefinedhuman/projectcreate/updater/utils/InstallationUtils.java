package de.undefinedhuman.projectcreate.updater.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class InstallationUtils {

    public static void loadInstallationDirectory(boolean firstRun, Setting installationPath, FsFile defaultInstallationDirectory) {
        ArrayList<String> errorMessages = FileError.checkFileForErrors(installationPath.getFile(), FileError.NULL, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE);
        Log.info(firstRun);
        if(firstRun || errorMessages.size() > 0)
            installationPath.setValue(chooseInstallationDirectory(defaultInstallationDirectory));
    }

    public static String chooseInstallationDirectory(FsFile defaultInstallationDirectory) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a installation directory...");
        defaultInstallationDirectory.mkdirs();
        ArrayList<String> errorMessages = FileError.checkFileForErrors(defaultInstallationDirectory, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE);
        if(errorMessages.size() > 0)
            Log.crash("Error", "An error occurred while creating the default installation directory! \nPlease choose another writable installation directory!", false);
        else chooser.setCurrentDirectory(defaultInstallationDirectory.file());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            Log.crash("Crash", "Please choose a writable installation directory!\nPlease restart!", true);
        FsFile selectedFile = new FsFile(chooser.getSelectedFile().getAbsolutePath(), Files.FileType.Absolute);
        if(!selectedFile.file().equals(defaultInstallationDirectory.file()) && defaultInstallationDirectory.delete())
            Log.info("Deleted default installation directory!");
        if(FileError.checkFileForErrors("checking installation directory", selectedFile, FileError.NULL, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE))
            Log.crash("Crash", "Please choose a writable installation directory!\nPlease restart!", true);
        return selectedFile.file().getAbsolutePath();
    }

    public static ArrayList<Version> fetchAvailableVersions(String url) {
        ArrayList<Version> availableVersions = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Log.instance.error("Error", "Error while connecting to download server:", ex);
        }
        if(doc == null)
            return availableVersions;
        for (Element file : doc.select("pre a")) {
            String fileName = file.attr("href");
            if(!fileName.startsWith("v"))
                continue;
            availableVersions.add(Version.parse(fileName.split(DownloadUtils.DOWNLOAD_FILE_EXTENSION)[0]));
        }
        return availableVersions;
    }

    public static boolean checkForSufficientSpace(String downloadURL, FsFile installationDirectory) {
        installationDirectory.mkdirs();
        if(!installationDirectory.exists() || !installationDirectory.file().canWrite())
            return false;
        Version newestVersion = Collections.max(fetchAvailableVersions(downloadURL));
        long fileSize = DownloadUtils.fetchFileSize(downloadURL + newestVersion.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION);
        return installationDirectory.file().getUsableSpace() > fileSize;
    }

    public static boolean checkIfVersionAlreadyDownloaded(String downloadURL, FsFile installationDirectory, Version version) {
        for(FileHandle installedVersion : installationDirectory.list(pathname -> pathname.getName().endsWith(DownloadUtils.DOWNLOAD_FILE_EXTENSION))) {
            if(installedVersion.nameWithoutExtension().equalsIgnoreCase(version.toString())
                    && !installedVersion.isDirectory()
                    && installedVersion.name().substring(installedVersion.name().lastIndexOf(".")).equalsIgnoreCase(DownloadUtils.DOWNLOAD_FILE_EXTENSION)
                    && installedVersion.length() == DownloadUtils.fetchFileSize(downloadURL + version + DownloadUtils.DOWNLOAD_FILE_EXTENSION))
                return true;
        }
        return false;
    }

    public static void checkProjectDotDirectory() {
        FsFile projectDotDirectory = new FsFile(Paths.GAME_PATH, Files.FileType.External);
        projectDotDirectory.mkdirs();
        ArrayList<String> errorMessages = FileError.checkFileForErrors(projectDotDirectory, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE);
        if(errorMessages.size() > 0)
            Log.crash("Crash", "An error occurred while creating the root directory of the project. \nPlease contact the author.\n" + errorMessages, true);
    }

}
