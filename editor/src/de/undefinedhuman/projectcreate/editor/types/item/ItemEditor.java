package de.undefinedhuman.projectcreate.editor.types.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.editor.types.Editor;
import de.undefinedhuman.projectcreate.editor.types.item.ui.ItemSelectionPanel;
import de.undefinedhuman.projectcreate.editor.types.item.ui.ItemSettingsPanel;
import de.undefinedhuman.projectcreate.editor.utils.EditorUtils;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ItemEditor extends Editor {

    private ItemSelectionPanel itemSelectionPanel;
    private ItemSettingsPanel itemSettingsPanel;

    public ItemEditor() {
        super();

        ItemManager.getInstance().loadItems(
                Arrays.stream(new FsFile(Paths.ITEM_PATH, Files.FileType.Internal).list(File::isDirectory))
                        .filter(fileHandle -> Utils.isInteger(fileHandle.name()) != null)
                        .mapToInt(fileHandle -> Integer.parseInt(fileHandle.name())).toArray()
        );

        add(itemSelectionPanel = new ItemSelectionPanel() {
            @Override
            public void select(Integer id) {
                itemSettingsPanel.updateItem(id);
            }
        }, 0.15f);

        add(itemSettingsPanel = new ItemSettingsPanel(), 0.85f);
    }

    @Override
    public void init() {
        itemSelectionPanel.init();
    }

    @Override
    public void createMenuButtonsPanel(JPanel menuButtonPanel) {
        menuButtonPanel.setLayout(new GridLayout(1, 4, 5, 0));
        menuButtonPanel.add(SettingsUI.createButton("Save", 0, e -> EditorUtils.saveItem(itemSelectionPanel.getSelectedItems().stream().mapToInt(id -> id).toArray())));
        menuButtonPanel.add(SettingsUI.createButton("Save All", 0, e -> EditorUtils.saveItem(ItemManager.getInstance().getItems().keySet().stream().mapToInt(id -> id).toArray())));
        menuButtonPanel.add(SettingsUI.createButton("Reset", 0, e -> {
            List<Integer> selectedIDs = itemSelectionPanel.getSelectedItems();
            if(selectedIDs.size() == 0)
                return;
            itemSettingsPanel.clear();
            itemSelectionPanel.getSelectedItems().forEach(id -> {
                ItemManager.getInstance().removeItems(id);
                ItemManager.getInstance().loadItems(id);
            });
            itemSelectionPanel.select(itemSelectionPanel.getSelectedIndex());
        }));
        menuButtonPanel.add(SettingsUI.createButton("Delete", 0, e -> {
            List<Integer> removedIDs = itemSelectionPanel.getSelectedItems();
            if(removedIDs.size() == 0)
                return;
            String removeItemIDsMessage = Arrays.toString(removedIDs.stream().map(id -> {
                Item selectedItem = ItemManager.getInstance().getItem(id);
                if(selectedItem == null || selectedItem.name == null)
                    return id.toString();
                return id + (!selectedItem.name.getValue().equalsIgnoreCase("") ? " " + selectedItem.name.getValue() : "");
            }).toArray(String[]::new));
            int result = JOptionPane.showConfirmDialog(null, "Delete Item" + Utils.appendSToString(removedIDs.size()) + " " + removeItemIDsMessage, "Are you sure you want to delete " + (removedIDs.size() > 1 ? "those" : "this") +  " item" + Utils.appendSToString(removedIDs.size()) + "?",  JOptionPane.YES_NO_OPTION);
            if(result != 0)
                return;
            FileUtils.deleteFile(removedIDs.stream().map(id -> new FsFile(Paths.ITEM_PATH, id + Variables.FILE_SEPARATOR, Files.FileType.Local)).filter(FileHandle::exists).toArray(FsFile[]::new));

            ItemManager.getInstance().removeItems(removedIDs.stream().mapToInt(value -> value).toArray());
            itemSettingsPanel.clear();
            itemSelectionPanel.removeSelected();
        }));
    }

}
