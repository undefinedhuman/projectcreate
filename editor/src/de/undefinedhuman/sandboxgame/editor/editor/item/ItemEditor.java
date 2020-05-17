package de.undefinedhuman.sandboxgame.editor.editor.item;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.sandboxgame.editor.editor.Editor;
import de.undefinedhuman.sandboxgame.editor.engine.ressources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.file.*;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ItemEditor extends Editor {

    public JComboBox<ItemType> itemComboBox;
    public Item currentItem;

    public JFrame loadWindow;
    public JComboBox<String> itemSelection;
    public JButton loadButton;

    public ItemEditor(Container container) {

        super(container);

        itemComboBox = new JComboBox<>(ItemType.values());
        itemComboBox.setSelectedIndex(0);
        itemComboBox.addActionListener(e -> {
            if(itemComboBox.getSelectedItem() == null) return;
            Tools.removeSettings(settingsPanel);
            ItemType type = ItemType.valueOf(itemComboBox.getSelectedItem().toString());
            if(type == null) return;
            currentItem = type.createInstance();
            Tools.addSettings(settingsPanel, currentItem.getSettings());
        });

        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(BorderFactory.createTitledBorder("Type:"));
        middlePanel.setBounds(545, 25, 200, 620);
        middlePanel.add(itemComboBox);

        container.add(middlePanel);

    }

    @Override
    public void load() {

        FileHandle[] itemDirs = ResourceManager.loadDir(Paths.ITEM_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle itemDir : itemDirs) {
            if (!itemDir.isDirectory()) continue;
            FileReader reader = new FileReader(new FsFile(Paths.ITEM_PATH, itemDir.name() + "/settings.item", false), true);
            reader.nextLine();
            reader.nextLine();
            HashMap<String, LineSplitter> settings = Tools.loadSettings(reader);
            ids.add(itemDir.name() + "-" + settings.get("Name").getNextString());
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
            reader.nextLine();
            ItemType type = ItemType.valueOf(reader.getNextString());
            if(type == null) return;
            reader.nextLine();
            itemComboBox.setSelectedItem(type);
            Tools.removeSettings(settingsPanel);
            currentItem = type.createInstance();
            HashMap<String, LineSplitter> settingsList = Tools.loadSettings(reader);
            for(Setting setting : currentItem.getSettings()) setting.loadSetting(reader.getParentDirectory(), settingsList);
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
        File file = new File(Paths.ITEM_PATH.getPath() + currentItem.getSettings().get(0).getInt() + "/");
        if(file.exists()) { FileUtils.deleteFile(file); Log.info("Item directory deleted successfully!"); }

        FsFile entityDir = new FsFile(Paths.ITEM_PATH, String.valueOf(currentItem.getSettings().get(0).getString()), true);
        FsFile settingsFile = new FsFile(entityDir, "/settings.item", false);

        FileWriter writer = settingsFile.getFileWriter(true);
        writer.writeString(currentItem.type.name());
        writer.nextLine();
        Tools.saveSettings(writer, currentItem.getSettings());
        writer.close();
    }

    public void reset() {
        itemComboBox.setSelectedIndex(0);
        currentItem = null;
        Tools.removeSettings(settingsPanel);
    }

}
