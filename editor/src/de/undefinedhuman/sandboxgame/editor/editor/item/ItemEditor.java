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
import java.util.HashMap;

public class ItemEditor extends Editor {

    private JComboBox<ItemType> itemComboBox;
    private Item currentItem;

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

        JFrame chooseWindow = new JFrame("Load Item");
        chooseWindow.setSize(480, 150);
        chooseWindow.setLocationRelativeTo(null);
        chooseWindow.setResizable(false);

        JLabel label = new JLabel();
        chooseWindow.add(label);

        JButton button = new JButton("Load Item");
        button.setBounds(90, 70, 300, 25);

        JComboBox<String> comboBox = new JComboBox<>(ids.toArray(new String[0]));
        comboBox.setBounds(90, 35, 300, 25);

        button.addActionListener(a -> {
            if(comboBox.getSelectedItem() == null) return;
            FileReader reader = new FileReader(ResourceManager.loadFile(Paths.ITEM_PATH, Integer.parseInt(((String) comboBox.getSelectedItem()).split("-")[0]) + "/settings.item"), true);
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
            chooseWindow.setVisible(false);
            reader.close();

        });

        label.add(comboBox);
        label.add(button);
        chooseWindow.setVisible(true);

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
        Tools.saveSetting(writer, currentItem.getSettings());
        writer.close();
    }

}
