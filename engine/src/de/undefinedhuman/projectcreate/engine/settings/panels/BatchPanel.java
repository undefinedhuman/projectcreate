package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.utils.JsonReader;
import de.undefinedhuman.projectcreate.engine.entity.components.sprite.SpriteLayer;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public abstract class BatchPanel<T extends SpriteLayer> extends StringPanel<T> {

    protected static JsonReader reader = new JsonReader();

    public BatchPanel(String name, T panelObject) {
        super(name, panelObject);
    }

    @Override
    protected void createObjectNameField(JPanel panel, int x, int y, int width, int height) {
        super.createObjectNameField(panel, x + width/2 + Variables.OFFSET, y, width/2 - Variables.OFFSET, height);
        JButton loadTextureLayers = new JButton("Load Layers");
        loadTextureLayers.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new FsFile(Paths.EDITOR_PATH, Files.FileType.Internal).file());
            chooser.setFileFilter(new FileNameExtensionFilter("Sprite Layer Data", "json"));

            int returnVal = chooser.showOpenDialog(null);
            if(returnVal != JFileChooser.APPROVE_OPTION) return;
            File dataFile = chooser.getSelectedFile();
            if(dataFile == null) {
                Log.showErrorDialog(Level.ERROR, "Please select a valid json data file!", false);
                return;
            }
            loadBatch(dataFile);
        });
        loadTextureLayers.setBounds(x, y, width/2 - Variables.OFFSET, height);
        panel.add(loadTextureLayers);
    }

    public abstract void loadBatch(File file);
}
