package de.undefinedhuman.projectcreate.editor.editor.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.editor.Window;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.editor.editor.item.ui.ItemSelectionPanel;
import de.undefinedhuman.projectcreate.editor.editor.item.ui.ItemSettingsPanel;
import de.undefinedhuman.projectcreate.editor.editor.utils.Utils;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
                        .filter(fileHandle -> Tools.isInteger(fileHandle.name()) != null)
                        .mapToInt(fileHandle -> Integer.parseInt(fileHandle.name())).toArray()
        );

        add(itemSelectionPanel = new ItemSelectionPanel() {
            @Override
            public void select(Integer id) {
                itemSettingsPanel.updateItem(ItemManager.getInstance().getItem(id));
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
        menuButtonPanel.setLayout(new GridLayout(1, 3, 5, 0));
        menuButtonPanel.setMinimumSize(new Dimension(100, Window.MENU_HEIGHT));
        menuButtonPanel.add(createUtilityButton("Save", e -> itemSelectionPanel.getSelectedItems().forEach(Utils::saveItem)));
        menuButtonPanel.add(createUtilityButton("Reset", e -> {
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
        menuButtonPanel.add(createUtilityButton("Delete", e -> {
            List<Integer> removedIDs = itemSelectionPanel.getSelectedItems();
            if(removedIDs.size() == 0)
                return;
            String removeItemIDsMessage = Arrays.toString(removedIDs.stream().map(id -> {
                Item selectedItem = ItemManager.getInstance().getItem(id);
                if(selectedItem == null || selectedItem.name == null)
                    return id.toString();
                return id + (!selectedItem.name.getValue().equalsIgnoreCase("") ? " " + selectedItem.name.getValue() : "");
            }).toArray(String[]::new));
            int result = JOptionPane.showConfirmDialog(null, "Delete Item" + Tools.appendSToString(removedIDs.size()) + " " + removeItemIDsMessage, "Are you sure you want to delete " + (removedIDs.size() > 1 ? "those" : "this") +  " item" + Tools.appendSToString(removedIDs.size()) + "?",  JOptionPane.YES_NO_OPTION);
            if(result != 0)
                return;
            FileUtils.deleteFile(removedIDs.stream().map(id -> new FsFile(Paths.ITEM_PATH, id + Variables.FILE_SEPARATOR, Files.FileType.Local)).filter(FileHandle::exists).toArray(FsFile[]::new));

            ItemManager.getInstance().removeItems(removedIDs.stream().mapToInt(value -> value).toArray());
            itemSettingsPanel.clear();
            itemSelectionPanel.removeSelected();
        }));
    }

    private JButton createUtilityButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        return button;
    }

}
