package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.utils.JsonReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class AsepriteUtils {

    public static final JsonReader JSON_READER = new JsonReader();

    public static void loadAsepriteFile(Consumer<FsFile> handleData) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new FsFile(Paths.EDITOR_PATH, Files.FileType.Local).file());
        chooser.setFileFilter(new FileNameExtensionFilter("Sprite", "aseprite"));

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal != JFileChooser.APPROVE_OPTION) return;
        File asepriteFile = chooser.getSelectedFile();
        if(asepriteFile == null || asepriteFile.isDirectory()) {
            Log.error("Please select a valid aseprite data file!");
            return;
        }

        FsFile exportFile = new FsFile(Paths.EDITOR_PATH, "export/", Files.FileType.Local);
        if(exportFile.exists()) exportFile.emptyDirectory();

        Process process;
        try {
            process = Runtime.getRuntime().exec("zsh " + new FsFile(Paths.EDITOR_PATH, "scripts/export.sh", Files.FileType.Internal).file().getAbsolutePath() + " " + asepriteFile.getAbsolutePath() + " " + exportFile.file().getAbsolutePath());
            InputStream inputStream = process.getInputStream();
            while (inputStream.read() != -1);
            process.waitFor();
        } catch (IOException | InterruptedException ex) {
            Log.error("Exception encountered", ex);
        }

        FsFile dataFile = new FsFile(exportFile, "data.json");
        if(!dataFile.exists())
            Log.error("Error while exporting sprite layers and creating data file");
        else handleData.accept(dataFile);

        if(exportFile.exists() && exportFile.isDirectory())
            exportFile.deleteDirectory();
    }

}
