package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.utils.JsonReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public abstract class BatchPanel<T extends PanelObject> extends StringPanel<T> {

    protected static JsonReader reader = new JsonReader();

    public BatchPanel(String name, T panelObject) {
        super(name, panelObject);
    }

    @Override
    protected void createUtilityButtons(JPanel panel, int y, int width) {
        super.createUtilityButtons(panel, y, width);
        panel.add(createButton("Load Layers", 0, y, width/3f*2f - Variables.OFFSET/2f, e -> {
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
        }));
    }

    public abstract void loadBatch(File file);
}
