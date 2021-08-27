package de.undefinedhuman.projectcreate.updater.utils;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.utils.Stage;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstallationUtils {

    public static void loadInstallationDirectory(boolean firstRun, FilePathSetting installationPath, FsFile defaultInstallationDirectory) {
        ArrayList<String> errorMessages = FileError.checkFileForErrors(installationPath.getValue(), FileError.NULL, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE);
        if(firstRun || !errorMessages.isEmpty())
            installationPath.setValue(chooseInstallationDirectory(defaultInstallationDirectory));
    }

    public static FsFile chooseInstallationDirectory(FsFile defaultInstallationDirectory) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a installation directory...");
        defaultInstallationDirectory.mkdirs();
        ArrayList<String> errorMessages = FileError.checkFileForErrors(defaultInstallationDirectory, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE);
        if(!errorMessages.isEmpty())
            Log.showErrorDialog("An error occurred while creating the default installation directory! \nPlease choose another writable installation directory!", false);
        else chooser.setCurrentDirectory(defaultInstallationDirectory.file());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            Log.showErrorDialog("Please choose a writable installation directory!\nPlease restart!", true);
        FsFile selectedFile = new FsFile(chooser.getSelectedFile().getAbsolutePath(), Files.FileType.Absolute);
        if(!selectedFile.file().equals(defaultInstallationDirectory.file()) && defaultInstallationDirectory.delete())
            Log.info("Deleted default installation directory!");
        if(FileError.checkFileForErrors("checking installation directory", selectedFile, FileError.NULL, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE))
            Log.showErrorDialog("Please choose a writable installation directory!\nPlease restart!", true);
        return selectedFile;
    }

    public static List<Version> fetchAvailableVersions(String url, Stage excludedStages) {
        ArrayList<Version> availableVersions = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Log.error("Error while connecting to download server:", ex);
        }
        if(doc == null)
            return availableVersions;
        for (Element file : doc.select("pre a")) {
            String fileName = file.attr("href");
            StringBuilder regex = new StringBuilder("^(");
            Arrays.stream(Stage.values()).forEach(stage -> regex.append(stage.name().toLowerCase()).append("|"));
            regex.deleteCharAt(regex.length()-1).append(").*");
            if(!fileName.matches(regex.toString()))
                continue;
            Version version = Version.parse(fileName.split(DownloadUtils.DOWNLOAD_FILE_EXTENSION)[0]);
            if(version.getStage().equals(excludedStages))
                continue;
            availableVersions.add(version);
        }
        return availableVersions;
    }

    public static boolean hasSufficientSpace(String downloadURL, FsFile installationDirectory, Version version) {
        installationDirectory.mkdirs();
        if(!installationDirectory.exists() || !installationDirectory.file().canWrite())
            return false;
        long fileSize = DownloadUtils.fetchFileSize(downloadURL + version.toString() + DownloadUtils.DOWNLOAD_FILE_EXTENSION);
        return installationDirectory.file().getUsableSpace() > fileSize;
    }

    public static boolean isVersionDownloaded(String downloadURL, FsFile installationDirectory, Version version) {
        FsFile installedVersion = new FsFile(installationDirectory.path(), version + DownloadUtils.DOWNLOAD_FILE_EXTENSION, Files.FileType.Absolute);
        return installedVersion.nameWithoutExtension().equalsIgnoreCase(version.toString())
                && !installedVersion.isDirectory()
                && installedVersion.name().substring(installedVersion.name().lastIndexOf(".")).equalsIgnoreCase(DownloadUtils.DOWNLOAD_FILE_EXTENSION)
                && installedVersion.length() == DownloadUtils.fetchFileSize(downloadURL + version + DownloadUtils.DOWNLOAD_FILE_EXTENSION);
    }

    public static void checkProjectDotDirectory() {
        FsFile projectDotDirectory = new FsFile(Paths.GAME_PATH, Files.FileType.External);
        projectDotDirectory.mkdirs();
        ArrayList<String> errorMessages = FileError.checkFileForErrors(projectDotDirectory, FileError.NON_EXISTENT, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE);
        if(!errorMessages.isEmpty())
            Log.showErrorDialog("An error occurred while creating the root directory of the project. \nPlease contact the author.\n" + errorMessages, true);
    }

}
