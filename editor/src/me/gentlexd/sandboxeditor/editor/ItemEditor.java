package me.gentlexd.sandboxeditor.editor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.Setting;
import me.gentlexd.sandboxeditor.engine.file.FileReader;
import me.gentlexd.sandboxeditor.engine.file.FileWriter;
import me.gentlexd.sandboxeditor.engine.file.FsFile;
import me.gentlexd.sandboxeditor.engine.file.Paths;
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

public class ItemEditor extends Editor implements ActionListener {

    private JComboBox itemComboBox;

    public ItemEditor(Container container) {

        super(container);

        path = Paths.ITEM_PATH;

        itemComboBox = new JComboBox(ItemType.values());
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

    public void loadItem() {

        FileHandle dir = RessourceManager.loadDir(Paths.ITEM_PATH);
        FileHandle[] handle = dir.list();
        String[] ids = new String[handle.length];

        for(int i = 0; i < ids.length; i++) {

            if(handle[i].isDirectory()) {

                FileHandle file = RessourceManager.loadFile(Paths.ITEM_PATH, handle[i].name() + "/settings.txt");
                FileReader reader = new FileReader(file, true);
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

        final JComboBox comboBox = new JComboBox(ids);
        comboBox.setBounds(90,35,300,25);

        button.addActionListener(arg0 -> {

            if(comboBox.getSelectedItem() != null) {

                FileHandle file = RessourceManager.loadFile(Paths.ITEM_PATH, comboBox.getSelectedItem().toString().split("-")[0] + "/settings.txt");
                FileReader reader = new FileReader(file,true);
                reader.nextLine();
                itemComboBox.setSelectedItem(ItemType.valueOf(reader.getNextString()));
                settings.get(0).setValue(Integer.parseInt(comboBox.getSelectedItem().toString().split("-")[0]));
                for(int i = 1; i < settings.size(); i++) settings.get(i).load(reader, Integer.valueOf(comboBox.getSelectedItem().toString().split("-")[0]));
                loadPopUp.setVisible(false);
                reader.close();

            } else loadPopUp.setVisible(false);

        });

        label.add(comboBox);
        label.add(button);

        loadPopUp.setVisible(true);

    }

    public void saveItem() {

        File file = new File(Paths.ITEM_PATH.getPath() + settings.get(0).getValue());

        if(file.exists()) { file.delete(); Log.info("Dir wurde gelöscht!"); }
        FsFile dir = new FsFile(Paths.ITEM_PATH, String.valueOf(settings.get(0).getValue()), true);
        FsFile fileN = new FsFile(dir,"/settings.txt",false);
        FileWriter writer = fileN.getFileWriter(true);
        writer.writeString(((ItemType) itemComboBox.getSelectedItem()).name());
        for(int i = 1; i < settings.size(); i++) settings.get(i).save(writer);

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

        switch (ItemType.valueOf(itemComboBox.getSelectedItem().toString())) {

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
        }

        int i = 0, i2 = 0;
        for(Setting setting : settings) {

            if(setting.getPanel().equals(mainPanel)) {

                setting.setPosition(new Vector2(40, 20 + 30 * i));
                i++;

            } else {

                setting.setPosition(new Vector2(40, 20 + 30 * i2));
                i2++;

            }

        }

    }

}
