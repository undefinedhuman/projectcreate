package de.undefinedhuman.projectcreate.core.editor.editor.item;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.editor.editor.Editor;
import de.undefinedhuman.projectcreate.core.engine.file.*;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.core.engine.items.Item;
import de.undefinedhuman.projectcreate.core.engine.items.ItemType;
import de.undefinedhuman.projectcreate.core.engine.resources.ResourceManager;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.core.engine.utils.Tools;
import de.undefinedhuman.projectcreate.core.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ItemEditor extends Editor {

    public JComboBox<ItemType> itemComboBox;
    public Item currentItem;

    public JFrame loadWindow;
    public JComboBox<String> itemSelection;
    public JButton loadButton;

    public ItemEditor(Container container) {
        super(container);
        itemComboBox = new JComboBox<>(ItemType.values());
        itemComboBox.setBounds(20, 25, 150, 25);
        itemComboBox.setSelectedIndex(0);
        itemComboBox.addActionListener(e -> {
            if(itemComboBox.getSelectedItem() == null) return;
            Tools.removeSettings(settingsPanel);
            ItemType type = ItemType.valueOf(itemComboBox.getSelectedItem().toString());
            currentItem = type.createInstance();
            Tools.addSettings(settingsPanel, currentItem.getSettings());
        });

        middlePanel.add(itemComboBox);

    }

    @Override
    public void load() {

        FileHandle[] itemDirs = ResourceManager.loadDir(Paths.ITEM_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle itemDir : itemDirs) {
            if (!itemDir.isDirectory()) continue;
            FsFile itemFile = new FsFile(Paths.ITEM_PATH, itemDir.name() + "/settings.item", Files.FileType.Internal);
            if(itemFile.isEmpty()) continue;
            FileReader reader = itemFile.getFileReader(true);
            SettingsObject settings = Tools.loadSettings(reader);
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
            FileReader reader = new FileReader(ResourceManager.loadFile(Paths.ITEM_PATH, Integer.parseInt(((String) itemSelection.getSelectedItem()).split("-")[0]) + "/settings.item"), true);
            SettingsObject settingsObject = Tools.loadSettings(reader);
            if(!settingsObject.containsKey("Type")) return;
            ItemType type = ItemType.valueOf(((LineSplitter) settingsObject.get("Type")).getNextString());
            itemComboBox.setSelectedItem(type);
            Tools.removeSettings(settingsPanel);
            currentItem = type.createInstance();
            for(Setting setting : currentItem.getSettings())
                setting.loadSetting(reader.getParentDirectory(), settingsObject);
            Tools.addSettings(settingsPanel, currentItem.getSettings());
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
        FsFile itemDir = new FsFile(Paths.ITEM_PATH, currentItem.getSettings().get(0).getInt() + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(itemDir.exists())
            FileUtils.deleteFile(itemDir);

        FileWriter writer = new FsFile(itemDir.path(), "settings.item", Files.FileType.Local).getFileWriter(true);
        writer.writeString("Type").writeString(currentItem.type.name());
        writer.nextLine();
        Tools.saveSettings(writer, currentItem.getSettings());
        writer.close();
    }

}
