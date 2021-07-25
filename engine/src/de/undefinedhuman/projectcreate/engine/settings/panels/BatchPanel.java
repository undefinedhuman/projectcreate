package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.utils.JsonReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public abstract class BatchPanel<T extends PanelObject> extends StringPanel<T> {

    private static final float BUTTON_WIDTH = 0.6f;

    protected static JsonReader reader = new JsonReader();

    public BatchPanel(String name, Class<T> panelObjectClass) {
        super(name, panelObjectClass);
    }

    @Override
    protected void createUtilityButtons(JPanel panel, float remainingWidth) {
        panel.add(createButton("Load Layers", e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new FsFile(Paths.EDITOR_PATH, Files.FileType.Internal).file());
            chooser.setFileFilter(new FileNameExtensionFilter("Sprite Layer Data", "json"));

            int returnVal = chooser.showOpenDialog(null);
            if(returnVal != JFileChooser.APPROVE_OPTION) return;
            File dataFile = chooser.getSelectedFile();
            if(dataFile == null) {
                Log.error("Please select a valid json data file!");
                return;
            }
            loadBatch(dataFile);
        }), BUTTON_WIDTH);
        super.createUtilityButtons(panel, remainingWidth - BUTTON_WIDTH);
    }

    public abstract void loadBatch(File file);
}
