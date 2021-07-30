package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.utils.JsonReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class BatchPanel<T extends PanelObject<String>> extends StringPanel<T> {

    private static final float BUTTON_WIDTH = 0.6f;

    protected static JsonReader reader = new JsonReader();

    public BatchPanel(String name, Class<T> panelObjectClass) {
        super(name, panelObjectClass);
    }

    @Override
    protected void createUtilityButtons(JPanel panel, float remainingWidth) {
        panel.add(SettingsUI.createButton("Aseprite", BUTTON_HEIGHT, e -> {
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
            else loadBatch(dataFile);

            if(exportFile.exists() && exportFile.isDirectory())
                exportFile.deleteDirectory();
        }), BUTTON_WIDTH);
        super.createUtilityButtons(panel, remainingWidth - BUTTON_WIDTH);
    }

    public abstract void loadBatch(FsFile file);
}
