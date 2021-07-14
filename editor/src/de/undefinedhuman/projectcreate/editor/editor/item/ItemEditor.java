package de.undefinedhuman.projectcreate.editor.editor.item;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.editor.Window;
import de.undefinedhuman.projectcreate.editor.editor.Editor;
import de.undefinedhuman.projectcreate.editor.editor.item.ui.ItemSelection;
import de.undefinedhuman.projectcreate.editor.editor.item.ui.ItemSettingsPanel;
import de.undefinedhuman.projectcreate.engine.file.FileUtils;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
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

    private ItemSelection<ItemType> itemSelectionPanel;
    private ItemSettingsPanel itemSettingsPanel;

    public ItemEditor() {
        super();

        ItemManager.getInstance().loadItems(
                Arrays.stream(new FsFile(Paths.ITEM_PATH, Files.FileType.Internal).list(File::isDirectory))
                        .filter(fileHandle -> Tools.isInteger(fileHandle.name()) != null)
                        .mapToInt(fileHandle -> Integer.parseInt(fileHandle.name())).toArray()
        );

        add(itemSelectionPanel = new ItemSelection<ItemType>(ItemType.values()) {
            @Override
            public void addItem(ItemType type) {
                int id = ItemManager.getInstance().getItems().keySet().stream().sorted().reduce((a, b) -> b).orElse(-1) + 1;
                Item item = type.createInstance();
                item.id.setValue(id);
                ItemManager.getInstance().addItem(id, item);
                saveItem(id);
            }

            @Override
            public void selectItem(int id) {
                itemSettingsPanel.updateItem(ItemManager.getInstance().getItem(id));
            }

            @Override
            public Integer[] getListData() {
                return ItemManager
                        .getInstance()
                        .getItems()
                        .keySet()
                        .toArray(new Integer[0]);
            }
        }, 0.15f);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.YELLOW);

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
            int selectedID = itemSelectionPanel.getSelectedItemID();
            if(selectedID == -1)
                return;
            saveItem(selectedID);
        }));
        menuButtonPanel.add(createUtilityButton("Reset", e -> {
            int selectedID = itemSelectionPanel.getSelectedItemID();
            if(selectedID == -1)
                return;
            ItemManager.getInstance().removeItems(selectedID);
            itemSettingsPanel.clear();
            ItemManager.getInstance().loadItems(selectedID);
            itemSelectionPanel.selectItem(selectedID);

        }));
        menuButtonPanel.add(createUtilityButton("Delete", e -> {
            int selectedID = itemSelectionPanel.getSelectedItemID();
            if(selectedID == -1)
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

    private void saveItem(int id) {
        Item item = ItemManager.getInstance().getItem(id);
        FsFile itemDir = new FsFile(Paths.ITEM_PATH, item.getSettingsList().getSettings().get(0).getValue() + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(itemDir.exists())
            FileUtils.deleteFile(itemDir);

        FileWriter writer = new FsFile(itemDir, "settings.item", Files.FileType.Local).getFileWriter(true);
        writer.writeString("Type").writeString(item.type.name());
        writer.nextLine();
        Tools.saveSettings(writer, item.getSettingsList());
        writer.close();
    }

    private void removeItemFromUI(int id) {
        ItemManager.getInstance().removeItems(id);
        itemSelectionPanel.removeSelectedItem();
        itemSettingsPanel.clear();
    }

}
