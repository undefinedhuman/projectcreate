package de.undefinedhuman.sandboxgame.editor.editor.entity;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.editor.editor.Editor;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.EntityType;
import de.undefinedhuman.sandboxgame.engine.file.*;
import de.undefinedhuman.sandboxgame.engine.resources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.Vector2Setting;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityEditor extends Editor {

    private JComboBox<ComponentType> componentComboBox;

    private DefaultListModel<String> componentList;
    private JList<String> listPanel;

    private ComponentType selectedComponent;

    private HashMap<ComponentType, ComponentBlueprint> components;

    private SettingsList entitySettings = new SettingsList();

    public EntityEditor(Container container) {
        super(container);
        components = new HashMap<>();

        componentComboBox = new JComboBox<>(ComponentType.values());
        componentComboBox.setSelectedItem(null);
        componentComboBox.setBounds(20, 25, 150, 25);

        entitySettings.addSettings(
                new Setting(SettingType.Int, "ID", 0),
                new Setting(SettingType.String, "Name", "Temp Name"),
                new Vector2Setting("Size", new Vector2(0, 0)),
                new SelectionSetting("Type", EntityType.values()));
        Tools.addSettings(mainPanel, entitySettings.getSettings());

        componentList = new DefaultListModel<>();

        listPanel = new JList<>(componentList);
        listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.addListSelectionListener(e -> {
            selectedComponent = listPanel.getSelectedValue() != null ? ComponentType.valueOf(listPanel.getSelectedValue()) : null;
            if(selectedComponent != null) {
                Tools.removeSettings(settingsPanel);
                Tools.addSettings(settingsPanel, components.get(selectedComponent).getSettings());
            }
        });

        JScrollPane listScroller = new JScrollPane(listPanel);
        listScroller.setBounds(20, 125, 150, 450);

        JButton addComponent = new JButton("Add");
        addComponent.setBounds(20, 55, 150, 25);
        addComponent.addActionListener(e -> {
            if(componentComboBox.getSelectedItem() == null) return;
            Tools.removeSettings(settingsPanel);
            ComponentType type = ComponentType.valueOf(componentComboBox.getSelectedItem().toString());
            if(componentList.contains(type.toString())) return;
            componentList.addElement(type.toString());
            components.put(type, type.createNewInstance());
        });

        JButton removeComponent = new JButton("Remove");
        removeComponent.setBounds(20, 85, 150, 25);
        removeComponent.addActionListener(e -> {
            if(listPanel.getSelectedValue() == null) return;
            ComponentType type = ComponentType.valueOf(listPanel.getSelectedValue());
            Tools.removeSettings(settingsPanel);
            components.remove(type);
            if(componentList.contains(type.toString())) componentList.removeElement(type.toString());
        });

        middlePanel.add(componentComboBox);
        middlePanel.add(addComponent);
        middlePanel.add(removeComponent);
        middlePanel.add(listScroller);

    }

    @Override
    public void load() {

        FileHandle[] entityDirs = ResourceManager.loadDir(Paths.ENTITY_FOLDER).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle entityDir : entityDirs) {
            /*if (!entityDir.isDirectory()) continue;
            FileReader reader = new FileReader(new FsFile(Paths.ENTITY_FOLDER, entityDir.name() + "/settings.entity", false), true);
            reader.nextLine();
            HashMap<String, LineSplitter[]> settings = Tools.loadSettings(reader);
            ids.add(entityDir.name() + "-" + settings.get("Name")[0].getNextString());
            reader.close();*/
        }

        JFrame chooseWindow = new JFrame("Load entity");
        chooseWindow.setSize(480,150);
        chooseWindow.setLocationRelativeTo(null);
        chooseWindow.setResizable(false);

        JLabel label = new JLabel();
        chooseWindow.add(label);

        JButton button = new JButton("Load Entity");
        button.setBounds(90, 70, 300, 25);

        JComboBox<String> comboBox = new JComboBox<>(ids.toArray(new String[0]));
        comboBox.setBounds(90, 35, 300, 25);

        button.addActionListener(a -> {
            /*if(comboBox.getSelectedItem() == null) return;

            componentList.clear();
            components.clear();
            Tools.removeSettings(settingsPanel);

            FileReader reader = new FileReader(ResourceManager.loadFile(Paths.ENTITY_FOLDER, Integer.parseInt(((String) comboBox.getSelectedItem()).split("-")[0]) + "/settings.entity"), true);
            reader.nextLine();
            HashMap<String, LineSplitter[]> settings = Tools.loadSettings(reader);
            for(Setting setting : entitySettings.getSettings()) setting.loadSetting(reader.getParentDirectory(), settings);

            int componentSize = reader.getNextInt();
            reader.nextLine();
            for(int i = 0; i < componentSize; i++) {
                ComponentType type = ComponentType.valueOf(reader.getNextString());
                if(componentList.contains(type.name())) return;
                componentList.addElement(type.name());
                reader.nextLine();
                components.put(type, type.load(reader));
            }

            chooseWindow.setVisible(false);
            reader.close();*/

        });

        label.add(comboBox);
        label.add(button);

        chooseWindow.setVisible(true);

    }

    @Override
    public void save() {
        FsFile entityDir = new FsFile(Paths.ENTITY_FOLDER, entitySettings.getSettings().get(0).getString() + Variables.FILE_SEPARATOR, true);
        if(entityDir.exists())
            FileUtils.deleteFile(entityDir);

        /*JSONObject entity = new JSONObject();
        entity.put("Base", Tools.saveSettings(entityDir, entitySettings.getSettings()));
        JSONArray components = new JSONArray();
        for(ComponentBlueprint componentBlueprint : this.components.values())
            components.add(componentBlueprint.save(entityDir));
        entity.put("Components", components);

        FileWriter writer = new FsFile(entityDir.createFile(true), "settings.entity", false).getFileWriter(true);
        writer.writeString(entity.toJSONString());
        writer.close();*/
    }

}
