package de.undefinedhuman.sandboxgame.editor.editor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.engine.file.*;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.engine.ressources.RessourceManager;
import me.gentlexd.sandboxeditor.items.Item;
import me.gentlexd.sandboxeditor.items.ItemType;
import me.gentlexd.sandboxeditor.items.type.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class ItemEditor extends Editor implements ActionListener {

    private JComboBox<ItemType> itemComboBox;

    public ItemEditor(Container container) {

        super(container);

        path = Paths.ITEM_PATH;

        itemComboBox = new JComboBox<>(ItemType.values());
        itemComboBox.setSelectedIndex(0);
        itemComboBox.addActionListener(this);

        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(BorderFactory.createTitledBorder("Type:"));
        middlePanel.setBounds(545, 25, 200, 620);
        middlePanel.add(itemComboBox);

        container.add(middlePanel);

    }

    @Override
    public void init() {}

    @Override
    public void render(SpriteBatch batch) {}

    @Override
    public void load() {

        FileHandle dir = RessourceManager.loadDir(Paths.ITEM_PATH);
        FileHandle[] handle = dir.list();
        String[] ids = new String[handle.length];

        for(int i = 0; i < ids.length; i++) {

            if(handle[i].isDirectory()) {

                FileHandle file = RessourceManager.loadFile(Paths.ITEM_PATH, handle[i].name() + "/settings.txt");
                System.out.println(handle.length);
                FileReader reader = new FileReader(file,true);
                reader.nextLine();

                ids[i] = handle[i].name() + "-" + reader.getNextString().toLowerCase() + "-" + reader.getNextString();

            }

        }

        final JFrame loadPopUp = new JFrame("");
        loadPopUp.setSize(480,150);
        loadPopUp.setLocationRelativeTo(null);
        loadPopUp.setResizable(false);

        JLabel label = new JLabel();
        loadPopUp.add(label);

        JButton button = new JButton("Load Item");
        button.setBounds(90,70,300,25);

        final JComboBox<String> comboBox = new JComboBox<>(ids);
        comboBox.setBounds(90,35,300,25);

        button.addActionListener(arg0 -> {

            if(comboBox.getSelectedItem() != null) {

                FileHandle file = RessourceManager.loadFile(Paths.ITEM_PATH, comboBox.getSelectedItem().toString().split("-")[0] + "/settings.txt");
                FileReader reader = new FileReader(file,true);
                reader.nextLine();
                itemComboBox.setSelectedItem(ItemType.valueOf(reader.getNextString()));
                int id = Integer.parseInt(comboBox.getSelectedItem().toString().split("-")[0]);
                settings.get(0).setValue(id);
                HashMap<String, LineSplitter> s = new HashMap<>();
                reader.nextLine();
                int settingsSize = reader.getNextInt();
                reader.nextLine();
                for (int i = 0; i < settingsSize; i++) {
                    s.put(reader.getNextString(), new LineSplitter(reader.nextLine(),true, ";"));
                    reader.nextLine();
                }
                reader.close();

                for(int i = 1; i < settings.size(); i++) settings.get(i).loadSetting(s, id);

            }

            loadPopUp.setVisible(false);

        });

        label.add(comboBox);
        label.add(button);

        loadPopUp.setVisible(true);

    }

    @Override
    public void save() {

        File file = new File(Paths.ITEM_PATH.getPath() + settings.get(0).getValue());

        if(file.exists()) { file.delete(); Log.info("Dir wurde gelöscht!"); }
        FsFile dir = new FsFile(Paths.ITEM_PATH, String.valueOf(settings.get(0).getValue()), true);
        FsFile fileN = new FsFile(dir,"/settings.txt",false);
        FileWriter writer = fileN.getFileWriter(true);
        ItemType type = ItemType.valueOf(String.valueOf(itemComboBox.getSelectedItem()));
        writer.writeString(type.name());
        writer.writeString((String) (type == ItemType.BLOCK ? settings.get(1).getValue() : settings.get(3).getValue()));
        writer.nextLine();
        writer.writeInt(settings.size()-1);
        writer.nextLine();
        for(int i = 1; i < settings.size(); i++) settings.get(i).saveSetting(writer);

        writer.close();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        settings.clear();
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        settingsPanel.removeAll();
        settingsPanel.revalidate();
        settingsPanel.repaint();

        switch (ItemType.valueOf(Objects.requireNonNull(itemComboBox.getSelectedItem()).toString())) {
            case ITEM:
                new Item().getSettings(ItemType.ITEM, settings, mainPanel, settingsPanel);
                break;
            case TOOL:
                new Tool().getSettings(ItemType.TOOL, settings, mainPanel, settingsPanel);
                break;
            case PICKAXE:
                new Pickaxe().getSettings(ItemType.PICKAXE, settings, mainPanel, settingsPanel);
                break;
            case WEAPON:
                new Weapon().getSettings(ItemType.WEAPON, settings, mainPanel, settingsPanel);
                break;
            case SWORD:
                new Sword().getSettings(ItemType.SWORD, settings, mainPanel, settingsPanel);
                break;
            case BLOCK:
                new Block().getSettings(ItemType.BLOCK, settings, mainPanel, settingsPanel);
                break;
            case BOW:
                new Bow().getSettings(ItemType.BOW, settings, mainPanel, settingsPanel);
                break;
            case STAFF:
                new Staff().getSettings(ItemType.STAFF, settings, mainPanel, settingsPanel);
                break;
            case ARMOR:
                new Armor().getSettings(ItemType.ARMOR, settings, mainPanel, settingsPanel);
            case HELMET:
                new Armor().getSettings(ItemType.HELMET, settings, mainPanel, settingsPanel);
        }

        int i = 0, i2 = 0;
        for(Setting setting : settings) setting.setPosition(new Vector2(40, 20 + 30 * (setting.getPanel().equals(mainPanel) ? i++ : i2++)));

    }

}
