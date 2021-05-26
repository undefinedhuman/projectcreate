package de.undefinedhuman.projectcreate.editor.editor.entity;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.ComponentType;
import de.undefinedhuman.projectcreate.editor.editor.ThreePanelEditor;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.EntityType;
import de.undefinedhuman.projectcreate.engine.file.*;
import de.undefinedhuman.projectcreate.engine.resources.RescourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectAdapter;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EntityEditor extends ThreePanelEditor {

    private JComboBox<ComponentType> componentComboBox;

    private DefaultListModel<String> componentList;
    private JList<String> listPanel;

    private String selectedComponent;

    private HashMap<String, ComponentBlueprint> components;

    private SettingsList baseSettings = new SettingsList();

    public EntityEditor(Container container) {
        super(container);
        components = new HashMap<>();

        componentComboBox = new JComboBox<>(ComponentType.values());
        componentComboBox.setSelectedItem(null);
        componentComboBox.setBounds(20, 25, 150, 25);

        baseSettings.addSettings(
                new IntSetting("ID", 0),
                new StringSetting("Name", "Temp Name"),
                new Vector2Setting("Size", new Vector2(0, 0)),
                new SelectionSetting<>("Type", EntityType.values(), value -> EntityType.valueOf(String.valueOf(value))));
        Tools.addSettings(leftPanel, baseSettings.getSettings().stream());

        componentList = new DefaultListModel<>();

        listPanel = new JList<>(componentList);
        listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.addListSelectionListener(e -> {
            selectedComponent = listPanel.getSelectedValue() != null ? listPanel.getSelectedValue() : null;
            if(selectedComponent != null) {
                Tools.removeSettings(rightPanel);
                Tools.addSettings(rightPanel, components.get(selectedComponent).getSettingsStream());
            }
        });

        JScrollPane listScroller = new JScrollPane(listPanel);
        listScroller.setBounds(20, 125, 150, 450);

        JButton addComponent = new JButton("Add");
        addComponent.setBounds(20, 55, 150, 25);
        addComponent.addActionListener(e -> {
            if(componentComboBox.getSelectedItem() == null) return;
            Tools.removeSettings(rightPanel);
            String componentName = String.valueOf(componentComboBox.getSelectedItem());
            if(componentList.contains(componentName))
                return;
            ComponentBlueprint blueprint = ComponentType.createNewInstance(ComponentType.valueOf(componentName.toUpperCase(Locale.ROOT)));
            if(blueprint == null)
                return;
            componentList.addElement(componentName);
            components.put(componentName, blueprint);
        });

        JButton removeComponent = new JButton("Remove");
        removeComponent.setBounds(20, 85, 150, 25);
        removeComponent.addActionListener(e -> {
            if(listPanel.getSelectedValue() == null)
                return;
            String componentName = String.valueOf(componentComboBox.getSelectedItem());
            Tools.removeSettings(rightPanel);
            components.remove(componentName);
            if(componentList.contains(componentName))
                componentList.removeElement(componentName);
        });

        middlePanel.add(componentComboBox);
        middlePanel.add(addComponent);
        middlePanel.add(removeComponent);
        middlePanel.add(listScroller);

    }

    @Override
    public void load() {

        FileHandle[] entityDirs = RescourceUtils.loadDir(Paths.ENTITY_PATH).list();
        ArrayList<String> ids = new ArrayList<>();

        for (FileHandle entityDir : entityDirs) {
            if (!entityDir.isDirectory()) continue;
            FsFile entityFile = new FsFile(Paths.ENTITY_PATH, entityDir.name() + "/settings.entity", Files.FileType.Internal);
            if(entityFile.length() == 0) continue;
            FileReader reader = entityFile.getFileReader(true);
            SettingsObject settings = new SettingsObjectAdapter(reader);
            ids.add(entityDir.name() + "-" + ((LineSplitter) settings.get("Name")).getNextString());
            reader.close();
        }

        Collections.reverse(ids);

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
            if(comboBox.getSelectedItem() == null) return;
            componentList.clear();
            components.clear();
            Tools.removeSettings(rightPanel);

            FileReader reader = new FileReader(new FsFile(Paths.ENTITY_PATH, Integer.parseInt(((String) comboBox.getSelectedItem()).split("-")[0]) + "/settings.entity", Files.FileType.Internal), true);
            SettingsObject settingsObject = new SettingsObjectAdapter(reader);

            for(Setting<?> setting : baseSettings.getSettings())
                setting.loadSetting(reader.parent(), settingsObject);

            for(ComponentType componentType : ComponentType.values()) {
                String componentBlueprintName = componentType.name().toUpperCase();
                if(!settingsObject.containsKey(componentBlueprintName))
                    continue;
                Object componentObject = settingsObject.get(componentBlueprintName);
                if(!(componentObject instanceof SettingsObject))
                    continue;
                ComponentBlueprint blueprint = ComponentType.createNewInstance(componentType);
                blueprint.load(reader.parent(), (SettingsObject) settingsObject.get(componentBlueprintName));
                componentList.addElement(componentBlueprintName);
                components.put(componentBlueprintName, blueprint);
            }

            chooseWindow.setVisible(false);
            reader.close();

        });

        label.add(comboBox);
        label.add(button);

        chooseWindow.setVisible(true);

    }

    @Override
    public void save() {
        FsFile entityDir = new FsFile(Paths.ENTITY_PATH, baseSettings.getSettings().get(0).getValue() + Variables.FILE_SEPARATOR, Files.FileType.Local);
        if(entityDir.exists())
            FileUtils.deleteFile(entityDir);

        FileWriter writer = new FsFile(entityDir, "settings.entity", Files.FileType.Local).getFileWriter(true);
        Tools.saveSettings(writer, baseSettings);
        for(ComponentBlueprint componentBlueprint : this.components.values())
            componentBlueprint.save(writer);
        writer.close();
    }

}
