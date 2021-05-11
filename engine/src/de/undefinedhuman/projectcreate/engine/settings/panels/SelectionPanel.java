package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SelectionPanel<T extends PanelObject> extends Panel<T> {

    private JComboBox<String> selection;

    public SelectionPanel(String name, T panelObject) {
        super(name, panelObject);
    }

    @Override
    protected void createPanelObjectNameComponent(JPanel panel, int width) {
        FileHandle[] itemDirs = ResourceManager.loadDir(Paths.ITEM_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle itemDir : itemDirs) {
            if (!itemDir.isDirectory()) continue;
            FileReader reader = new FileReader(new FsFile(Paths.ITEM_PATH, itemDir.name() + "/settings.item", Files.FileType.Internal), true);
            ids.add(itemDir.name() + "-" + ((LineSplitter) Tools.loadSettings(reader).get("Name")).getNextString());
            reader.close();
        }

        String[] idArray = ids.toArray(new String[0]);
        Arrays.sort(idArray, Comparator.comparing(c -> Integer.valueOf(c.split("-")[0])));

        selection = new JComboBox<>(idArray);
        selection.setBounds(0, 0, width, Panel.INPUT_HEIGHT);
        panel.add(selection);
    }

    @Override
    protected String getSelectedObjectName() {
        if(selection.getSelectedItem() == null) {
            Log.error("Please select a object from the panel!");
            return "";
        }
        return (String) selection.getSelectedItem();
    }

    @Override
    public void selectObject(T object, JPanel objectPanel, int containerWidth) {
        selection.setSelectedItem(object.getKey());
        Tools.removeSettings(objectPanel);
        Tools.addSettings(objectPanel, 0, 0, 0, containerWidth, object.getSettings().stream());
    }

}
