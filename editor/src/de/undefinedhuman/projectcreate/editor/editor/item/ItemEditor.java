package de.undefinedhuman.projectcreate.editor.editor.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.editor.editor.ThreePanelEditor;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.resources.RescourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectAdapter;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ItemEditor extends ThreePanelEditor {

    public JComboBox<ItemType> itemComboBox;
    public Item currentItem;

    public JFrame loadWindow;
    public JComboBox<String> itemSelection;
    public JButton loadButton;

    public ItemEditor(Container container, int width, int height) {
        super(container, width, height);
        itemComboBox = new JComboBox<>(ItemType.values());
        itemComboBox.setBounds(20, 25, 150, 25);
        itemComboBox.setSelectedIndex(0);
        itemComboBox.addActionListener(e -> {
            if(itemComboBox.getSelectedItem() == null) return;
            Tools.removeSettings(leftPanel, rightPanel);
            ItemType type = ItemType.valueOf(itemComboBox.getSelectedItem().toString());
            currentItem = type.createInstance();
            Tools.addSettings(leftPanel, currentItem.getSettings().subList(0, currentItem.getItemSettingsAmount()).stream());
            Tools.addSettings(rightPanel, currentItem.getSettings().subList(currentItem.getItemSettingsAmount(), currentItem.getSettings().size()).stream());
        });

        middlePanel.add(itemComboBox);

    }

    @Override
    public void load() {

        FileHandle[] itemDirs = RescourceUtils.loadDir(Paths.ITEM_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle itemDir : itemDirs) {
            if (!itemDir.isDirectory())
                continue;
            FsFile itemFile = new FsFile(Paths.ITEM_PATH, itemDir.name() + "/settings.item", Files.FileType.Internal);
            if(itemFile.length() == 0)
                continue;
            FileReader reader = itemFile.getFileReader(true);
            SettingsObject settings = new SettingsObjectAdapter(reader);
            ids.add(itemDir.name() + "-" + ((LineSplitter) settings.get("Name")).getNextString());
            reader.close();
        }

        loadWindow = new JFrame("Load Item");
        loadWindow.setSize(480, 150);
        loadWindow.setLocationRelativeTo(null);
        loadWindow.setResizable(false);

        JLabel label = new JLabel();
        loadWindow.add(label);

        loadButton = new JButton("Load Item");
        loadButton.setBounds(90, 70, 300, 25);

        String[] idArray = ids.toArray(new String[0]);
        Arrays.sort(idArray, Comparator.comparing(c -> Integer.valueOf(c.split("-")[0])));

        itemSelection = new JComboBox<>(idArray);
        itemSelection.setBounds(90, 35, 300, 25);

        loadButton.addActionListener(a -> {
            if(itemSelection.getSelectedItem() == null) return;
            FileReader reader = new FileReader(new FsFile(Paths.ITEM_PATH, Integer.parseInt(((String) itemSelection.getSelectedItem()).split("-")[0]) + "/settings.item", Files.FileType.Internal), true);
            SettingsObject settingsObject = new SettingsObjectAdapter(reader);
            if(!settingsObject.containsKey("Type")) return;
            ItemType type = ItemType.valueOf(((LineSplitter) settingsObject.get("Type")).getNextString());
            itemComboBox.setSelectedItem(type);
            Tools.removeSettings(leftPanel, rightPanel);
            currentItem = type.createInstance();
            for(Setting<?> setting : currentItem.getSettingsList().getSettings())
                setting.load(reader.parent(), settingsObject);
            Tools.addSettings(leftPanel, currentItem.getSettings().subList(0, currentItem.getItemSettingsAmount()).stream());
            Tools.addSettings(rightPanel, currentItem.getSettings().subList(currentItem.getItemSettingsAmount(), currentItem.getSettings().size()).stream());
            loadWindow.setVisible(false);
            reader.close();
        });

        label.add(itemSelection);
        label.add(loadButton);
        loadWindow.setVisible(true);
    }

    @Override
    public void save() {
        if(currentItem == null) return;
        FsFile itemDir = new FsFile(Paths.ITEM_PATH, currentItem.getSettingsList().getSettings().get(0).getValue() + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(itemDir.exists())
            FileUtils.deleteFile(itemDir);

        FileWriter writer = new FsFile(itemDir, "settings.item", Files.FileType.Local).getFileWriter(true);
        writer.writeString("Type").writeString(currentItem.type.name());
        writer.nextLine();
        Tools.saveSettings(writer, currentItem.getSettingsList());
        writer.close();
    }

}
