package de.undefinedhuman.projectcreate.core.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.engine.file.FileReader;
import de.undefinedhuman.projectcreate.core.engine.file.FsFile;
import de.undefinedhuman.projectcreate.core.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.core.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.core.engine.log.Log;
import de.undefinedhuman.projectcreate.core.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.core.engine.utils.Tools;

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
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        super.addValueMenuComponents(panel, position);
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
        selection.setBounds((int) position.x - 175, (int) position.y + 30, 370, 25);
        panel.add(selection);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addObject() {
        if(selection.getSelectedItem() == null) {
            Log.error("No selected Item!");
            return;
        }
        T object = null;
        try {
            object = (T) this.panelObject.getClass().newInstance();
            object.setKey((String) selection.getSelectedItem());
            if(objects.containsKey(object.getKey())) {
                Log.error(this.key + " with name " + object.getKey() + " already exist!");
                return;
            }
            objects.put(object.getKey(), object);
        } catch (InstantiationException | IllegalAccessException ex) { ex.printStackTrace(); }
        if(object != null) objectList.addElement(object.getKey());
    }

    @Override
    public void removeObject() {
        if(selection.getSelectedItem() == null || !objectList.contains(selection.getSelectedItem())) {
            Log.error(this.key + " with name " + selection.getSelectedItem() + " does not exist!");
            return;
        }
        objectList.removeElement(selection.getSelectedItem());
        objects.remove(String.valueOf(selection.getSelectedItem()));
    }

    @Override
    public void selectObject(T object) {
        selection.setSelectedItem(object.getKey());
        Tools.removeSettings(objectPanel);
        Tools.addSettings(objectPanel, object.getSettings());
    }

}
