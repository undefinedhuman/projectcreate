package de.undefinedhuman.projectcreate.editor.editor.item;

import com.badlogic.gdx.Files;
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
        menuButtonPanel.add(createUtilityButton("Save", e -> {
            Integer selectedID = itemSelectionPanel.getSelectedID();
            if(selectedID == null)
                return;
            Utils.saveItem(selectedID);
        }));
        menuButtonPanel.add(createUtilityButton("Reset", e -> {
            Integer selectedID = itemSelectionPanel.getSelectedID();
            if(selectedID == null)
                return;
            ItemManager.getInstance().removeItems(selectedID);
            itemSettingsPanel.clear();
            ItemManager.getInstance().loadItems(selectedID);
            itemSelectionPanel.select(selectedID);

        }));
        menuButtonPanel.add(createUtilityButton("Delete", e -> {
            Integer selectedID = itemSelectionPanel.getSelectedID();
            if(selectedID == null)
                return;
            Item selectedItem = ItemManager.getInstance().getItem(selectedID);
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Item " + selectedID + " " + selectedItem.name.getValue(), JOptionPane.YES_NO_OPTION);
            if(result != 0)
                return;
            FsFile itemDir = new FsFile(Paths.ITEM_PATH, selectedID + Variables.FILE_SEPARATOR, Files.FileType.Local);
            if(itemDir.exists()) FileUtils.deleteFile(itemDir);
            removeItemFromUI(selectedID);
        }));
    }

    private JButton createUtilityButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        return button;
    }

    private void removeItemFromUI(int id) {
        ItemManager.getInstance().removeItems(id);
        itemSelectionPanel.removeSelected();
        itemSettingsPanel.clear();
    }

}
