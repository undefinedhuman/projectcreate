package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

public abstract class Panel<T extends PanelObject> extends Setting {

    private JList<String> objectSelectionList;
    private JScrollPane objectScrollPane;

    protected HashMap<String, T> objects = new HashMap<>();
    protected DefaultListModel<String> objectList;
    protected T panelObject;
    protected JPanel objectPanel;

    public Panel(String key, T panelObject) {
        super(SettingType.Panel, key, "");
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
        objectPanel.setOpaque(true);

        objectList = new DefaultListModel<>();
        for(String key : objects.keySet()) objectList.addElement(key);

        objectSelectionList = new JList<>(objectList);
        objectSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectSelectionList.addListSelectionListener(e -> {
            if(objectSelectionList.getSelectedValue() == null) return;
            selectObject(objects.get(objectSelectionList.getSelectedValue()));
        });

        objectScrollPane = new JScrollPane(objectSelectionList);
        objectScrollPane.setBounds((int) position.x - 175, (int) position.y + 90, 370, 90);

        this.offset = 190 + panelObject.settings.getSettings().size() * 30;

        panel.add(objectScrollPane);
        panel.add(objectPanel);
        panel.add(addButton("Add", (int) position.x - 175, (int) position.y + 60, e -> addObject()));
        panel.add(addButton("Remove", (int) position.x + 15, (int) position.y + 60, e -> removeObject()));
    }

    private JButton addButton(String text, int x, int y, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 180, 25);
        button.addActionListener(listener);
        return button;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void load(FsFile parentDir, Object value) {
        if(!(value instanceof SettingsObject)) return;
        SettingsObject settingsObject = (SettingsObject) value;
        HashMap<String, Object> settings = settingsObject.getSettings();
        for(String key : settings.keySet()) {
            Object panelObjectSettings = settings.get(key);
            if(!(panelObjectSettings instanceof SettingsObject)) continue;
            try {
                objects.put(key, (T) panelObject.getClass().newInstance().load(parentDir, (SettingsObject) panelObjectSettings).setKey(key));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        for(PanelObject object : this.objects.values())
            object.save(writer);
        writer.writeString("}");
    }

    public HashMap<String, T> getPanelObjects() {
        return objects;
    }

    public Collection<T> values() {
        return objects.values();
    }

    public abstract void addObject();
    public abstract void removeObject();
    public abstract void selectObject(T object);

}
