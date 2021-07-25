package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectAdapter;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SelectionPanel<T extends PanelObject> extends Panel<T> {

    private JComboBox<String> selection;

    public SelectionPanel(String name, Class<T> panelObjectClass) {
        super(name, panelObjectClass);
    }

    @Override
    protected void createPanelObjectNameComponent(JPanel panel) {
        FileHandle[] itemDirs = RessourceUtils.loadDir(Paths.ITEM_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle itemDir : itemDirs) {
            if (!itemDir.isDirectory()) continue;
            FileReader reader = new FileReader(new FsFile(Paths.ITEM_PATH, itemDir.name() + "/settings.item", Files.FileType.Internal), true);
            ids.add(itemDir.name() + "-" + ((LineSplitter) new SettingsObjectAdapter(reader).get("Name")).getNextString());
            reader.close();
        }

        String[] idArray = ids.toArray(new String[0]);
        Arrays.sort(idArray, Comparator.comparing(c -> Integer.valueOf(c.split("-")[0])));

        selection = new JComboBox<>(idArray);
        selection.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        selection.setFont(selection.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        selection.setPreferredSize(new Dimension(0, Panel.INPUT_HEIGHT));
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
    public void selectObject(T object, Accordion panel) {
        selection.setSelectedItem(object.getKey());
        for(Setting<?> setting : object.getSettings())
            setting.createSettingUI(panel);
    }

}
