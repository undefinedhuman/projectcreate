package de.undefinedhuman.sandboxgame.engine.settings.panels;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FileWriter;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Panel extends Setting {

    private JTextField objectName;
    private JButton addObject, removeObject;

    private JPanel objectPanel;

    private DefaultListModel<String> objectList;
    private JList<String> objectSelectionList;
    private JScrollPane objectScrollPane;

    private HashMap<String, PanelObject> objects = new HashMap<>();
    private PanelObject panelObject;

    public Panel(SettingType type, String name, PanelObject panelObject) {
        super(type, name, "");
        this.panelObject = panelObject;
    }

    @Override
    protected void delete() {
        super.delete();
        for(PanelObject animation : objects.values()) animation.delete();
        objects.clear();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        objectPanel = new JPanel(null);
        objectPanel.setBounds((int) position.x - 175, (int) position.y + 190, 370, panelObject.settings.getSettings().size() * 25);
        objectPanel.setBackground(Color.WHITE);
        objectPanel.setOpaque(true);

        objectName = new JTextField("");
        objectName.setBounds((int) position.x - 175, (int) position.y + 30, 370, 25);

        objectList = new DefaultListModel<>();
        for(String key : objects.keySet()) objectList.addElement(key);
        objectSelectionList = new JList<>(objectList);
        objectSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectSelectionList.addListSelectionListener(e -> {
            if(objectSelectionList.getSelectedValue() == null) return;
            PanelObject currentObject = objects.get(objectSelectionList.getSelectedValue());
            objectName.setText(objectSelectionList.getSelectedValue());
            Tools.removeSettings(objectPanel);
            Tools.addSettings(objectPanel, currentObject.getSettings());
        });

        addObject = new JButton("Add");
        addObject.setBounds((int) position.x - 175, (int) position.y + 60, 180, 25);
        addObject.addActionListener(e -> {
            if(objectList.contains(objectName.getText())) return;
            objectList.addElement(objectName.getText());
            try { objects.put(objectName.getText(), panelObject.getClass().newInstance());
            } catch (InstantiationException | IllegalAccessException ex) { ex.printStackTrace(); }
        });

        removeObject = new JButton("Remove");
        removeObject.setBounds((int) position.x + 15, (int) position.y + 60, 180, 25);
        removeObject.addActionListener(e -> {
            if(!objectList.contains(objectName.getText())) return;
            objectList.removeElement(objectName.getText());
            objects.remove(objectName.getText());
        });

        objectScrollPane = new JScrollPane(objectSelectionList);
        objectScrollPane.setBounds((int) position.x - 175, (int) position.y + 90, 370, 90);

        panel.add(objectScrollPane);
        panel.add(objectPanel);
        panel.add(objectName);
        panel.add(addObject);
        panel.add(removeObject);
    }

    public void loadObjects(FileReader reader) {
        objects.clear();
        int size = reader.getNextInt();
        reader.nextLine();
        for(int i = 0; i < size; i++) {
            String key = reader.getNextString();
            reader.nextLine();
            try { objects.put(key, panelObject.getClass().newInstance().load(reader));
            } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
        }
        if(objectList != null) {
            objectList.clear();
            for(String key : objects.keySet()) objectList.addElement(key);
        }
    }

    public void saveObjects(FileWriter writer) {
        writer.writeInt(objects.size());
        writer.nextLine();
        for(String key : objects.keySet()) {
            writer.writeString(key);
            writer.nextLine();
            objects.get(key).save(writer);
        }
    }

    public HashMap<String, PanelObject> getObjects() {
        return objects;
    }

}
