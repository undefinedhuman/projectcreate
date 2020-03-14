package de.undefinedhuman.sandboxgame.editor.editor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.gentlexd.sandboxeditor.editor.settings.IntSetting;
import me.gentlexd.sandboxeditor.editor.settings.StringSetting;
import me.gentlexd.sandboxeditor.editor.settings.Vector2Setting;
import me.gentlexd.sandboxeditor.engine.file.*;
import me.gentlexd.sandboxeditor.engine.log.Log;
import me.gentlexd.sandboxeditor.engine.ressources.RessourceManager;
import me.gentlexd.sandboxeditor.entity.Component;
import me.gentlexd.sandboxeditor.entity.ComponentType;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class EntityEditor extends Editor {

    private DefaultListModel listModel;

    private JComboBox componentComnboBox, typeCombo;
    private JList listPanel;

    private ComponentType currentSelected;

    private HashMap<ComponentType, Component> components;

    public EntityEditor(Container container) {
        super(container);

        path = Paths.ENTITY_FOLDER;

        settings.add(new IntSetting(mainPanel,"ID",0));
        settings.get(0).setPosition(new Vector2(40, 20));

        typeCombo = new JComboBox(EntityType.values());
        typeCombo.setSelectedItem(null);
        typeCombo.setBounds(40,50,150,25);

        mainPanel.add(typeCombo);

        settings.add(new StringSetting(mainPanel,"Name","Temp"));
        settings.get(1).setPosition(new Vector2(40,80));

        settings.add(new Vector2Setting(mainPanel,"Size",new Vector2(0,0)));
        settings.get(2).setPosition(new Vector2(40,120));

        components = new HashMap<>();

        componentComnboBox = new JComboBox(ComponentType.values());
        componentComnboBox.setSelectedItem(null);
        componentComnboBox.setBounds(25, 25,150,25);

        listModel = new DefaultListModel();

        listPanel = new JList(listModel);
        listPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.addListSelectionListener(e -> {

            if(currentSelected != null) if(components.containsKey(currentSelected)) components.get(currentSelected).removeMenu();
            settingsPanel.removeAll();
            currentSelected = listPanel.getSelectedValue() != null ? ComponentType.valueOf(listPanel.getSelectedValue().toString()) : null;
            if(currentSelected != null) components.get(currentSelected).addMenu();

        });

        JScrollPane listScroller = new JScrollPane(listPanel);
        listScroller.setBounds(25,125,150,450);

        JButton addComonent = new JButton("Add");
        addComonent.setBounds(25,55,150,25);
        addComonent.addActionListener(e -> {

            if(componentComnboBox.getSelectedItem() != null) {

                settingsPanel.revalidate();
                settingsPanel.repaint();

                ComponentType type = ComponentType.valueOf(componentComnboBox.getSelectedItem().toString());
                if(!listModel.contains(type.toString())) {
                    listModel.addElement(type.toString());
                    components.put(type, ComponentType.getComponent(type, settingsPanel));
                }

            }

        });

        JButton removeComponent = new JButton("Remove");
        removeComponent.setBounds(25,85,150,25);
        removeComponent.addActionListener(e -> {

            if(listPanel.getSelectedValue() != null) {

                ComponentType type = ComponentType.valueOf(listPanel.getSelectedValue().toString());
                components.get(currentSelected).removeMenu();
                components.remove(type);
                if(listModel.contains(type.toString())) listModel.removeElement(type.toString());

            }

        });

        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(BorderFactory.createTitledBorder("Type:"));
        middlePanel.setLayout(null);
        middlePanel.setBounds(545, 25, 200, 620);
        middlePanel.add(componentComnboBox);
        middlePanel.add(addComonent);
        middlePanel.add(removeComponent);
        middlePanel.add(listScroller);

        container.add(middlePanel);

    }

    @Override
    public void init() {

    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void load() {

        listModel.clear();
        components.clear();

        FileHandle dir = RessourceManager.loadDir(Paths.ENTITY_FOLDER);
        FileHandle[] handle = dir.list();
        String[] ids = new String[handle.length];

        for(int i = 0; i < ids.length; i++) {

            if(handle[i].isDirectory()) {

                FileHandle file = RessourceManager.loadFile(Paths.ENTITY_FOLDER, handle[i].name() + "/settings.txt");
                FileReader reader = new FileReader(file,true);
                reader.nextLine();

                ids[i] = handle[i].name() + "-" + reader.getNextString();

            }

        }

        final JFrame loadPopUp = new JFrame("");
        loadPopUp.setSize(480,150);
        loadPopUp.setLocationRelativeTo(null);
        loadPopUp.setResizable(false);

        JLabel label = new JLabel();
        loadPopUp.add(label);

        JButton button = new JButton("Load Entity");
        button.setBounds(90,70,300,25);

        final JComboBox comboBox = new JComboBox(ids);
        comboBox.setBounds(90,35,300,25);

        button.addActionListener(arg0 -> {

            if(comboBox.getSelectedItem() != null) {

                FileHandle file = RessourceManager.loadFile(Paths.ENTITY_FOLDER, comboBox.getSelectedItem().toString().split("-")[0] + "/settings.txt");
                FileReader reader = new FileReader(file,true);
                settings.get(0).setValue(Integer.parseInt(comboBox.getSelectedItem().toString().split("-")[0]));
                reader.nextLine();
                typeCombo.setSelectedItem(EntityType.valueOf(reader.getNextString()));
                settings.get(1).setValue(reader.getNextString());
                settings.get(2).setValue(reader.getNextVector2());
                int size = reader.getNextInt();

                for(int i = 0; i < size; i++) {

                    reader.nextLine();

                    ComponentType type = ComponentType.valueOf(reader.getNextString());
                    if(!listModel.contains(type.toString())) {
                        listModel.addElement(type.toString());
                        components.put(type, ComponentType.getComponent(type, settingsPanel));
                        components.get(type).load(reader, Integer.parseInt(settings.get(0).getValue().toString()));
                    }

                }

                loadPopUp.setVisible(false);
                reader.close();

            } else loadPopUp.setVisible(false);

        });

        label.add(comboBox);
        label.add(button);

        loadPopUp.setVisible(true);

    }

    @Override
    public void save() {

        File file = new File(Paths.ENTITY_FOLDER.getPath() + settings.get(0).getValue() + "/");

        if(file.exists()) { FileManager.deleteFile(file); Log.info("Dir wurde gelöscht!"); }
        FsFile dir = new FsFile(Paths.ENTITY_FOLDER, String.valueOf(settings.get(0).getValue()),true);
        FsFile fileN = new FsFile(dir,"/settings.txt",false);
        FileWriter writer = fileN.getFileWriter(true);
        writer.writeString(((EntityType) Objects.requireNonNull(typeCombo.getSelectedItem())).name());
        writer.writeString(settings.get(1).getValue().toString());
        writer.writeVector2(((Vector2Setting) settings.get(2)).getVector());
        writer.writeInt(components.size());
        writer.nextLine();
        for(ComponentType type : components.keySet()) {

            writer.writeString(type.name());
            Component component = components.get(type);
            component.save(writer);

        }

        writer.close();

    }

}
