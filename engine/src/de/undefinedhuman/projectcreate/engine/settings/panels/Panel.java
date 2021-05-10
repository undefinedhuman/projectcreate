package de.undefinedhuman.projectcreate.engine.settings.panels;

import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

public abstract class Panel<T extends PanelObject> extends Setting {

    private static final int INPUT_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT;
    private static final int BUTTON_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT;
    private static final int OBJECT_PANEL_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT*4;

    private JList<String> objectSelectionList;

    protected HashMap<String, T> panelObjects = new HashMap<>();
    protected DefaultListModel<String> objectList;
    protected T panelObject;

    private JPanel objectPanel;

    public Panel(String key, T panelObject) {
        super(SettingType.Panel, key, "");
        this.panelObject = panelObject;
        setContentHeight(INPUT_HEIGHT + BUTTON_HEIGHT + OBJECT_PANEL_HEIGHT + Variables.OFFSET*3 + getPanelObjectContentHeight());
    }

    @Override
    protected void delete() {
        super.delete();
        for(PanelObject animation : panelObjects.values()) animation.delete();
        panelObjects.clear();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {
        objectList = new DefaultListModel<>();
        for(String key : panelObjects.keySet())
            objectList.addElement(key);

        objectSelectionList = new JList<>(objectList);
        objectSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectSelectionList.addListSelectionListener(e -> {
            if(objectSelectionList.getSelectedValue() == null)
                return;
            selectObject(panelObjects.get(objectSelectionList.getSelectedValue()), objectPanel, width);
        });

        JScrollPane objectSelectionPanel = new JScrollPane(objectSelectionList);
        objectSelectionPanel.setBounds(0, INPUT_HEIGHT + BUTTON_HEIGHT + Variables.OFFSET*2, width, OBJECT_PANEL_HEIGHT);

        objectPanel = new JPanel(null);
        objectPanel.setBounds(0, INPUT_HEIGHT + BUTTON_HEIGHT + OBJECT_PANEL_HEIGHT + Variables.OFFSET*3, width, getPanelObjectContentHeight());
        objectPanel.setOpaque(true);

        createObjectNameField(panel, 0, 0, width, INPUT_HEIGHT);
        panel.add(createButton("Add", 0, width, e -> {
            String name = getSelectedObjectName();
            if(name.equalsIgnoreCase(""))
                return;
            addPanelObject(name);
        }));
        panel.add(createButton("Remove", width/2 + Variables.OFFSET, width, e -> removeObject(objectPanel)));
        panel.add(objectSelectionPanel);
        panel.add(objectPanel);
    }

    private JButton createButton(String text, int x, int width, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, INPUT_HEIGHT + Variables.OFFSET, width/2 - Variables.OFFSET, BUTTON_HEIGHT);
        button.addActionListener(listener);
        return button;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadValue(FsFile parentDir, Object value) {
        if(!(value instanceof SettingsObject)) return;
        SettingsObject settingsObject = (SettingsObject) value;
        HashMap<String, Object> settings = settingsObject.getSettings();
        for(String key : settings.keySet()) {
            Object panelObjectSettings = settings.get(key);
            if(!(panelObjectSettings instanceof SettingsObject))
                continue;
            T panelObject = createNewInstance();
            if(panelObject == null)
                return;
            panelObjects.put(key, (T) panelObject.load(parentDir, (SettingsObject) panelObjectSettings).setKey(key));
        }
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        for(PanelObject object : this.panelObjects.values())
            object.save(writer);
        writer.writeString("}");
    }

    private int getPanelObjectContentHeight() {
        int height = 0;
        for(Setting setting : panelObject.getSettings())
            height += setting.getTotalHeight();
        return height;
    }

    public HashMap<String, T> getPanelObjects() {
        return panelObjects;
    }

    public Collection<T> values() {
        return panelObjects.values();
    }

    protected abstract void createObjectNameField(JPanel panel, int x, int y, int width, int height);
    protected abstract String getSelectedObjectName();
    protected abstract void removeObject(JPanel panel);
    protected abstract void selectObject(T object, JPanel panel, int containerWidth);

    protected void addPanelObject(String name) {
        addPanelObject(name, createNewInstance());
    }

    protected void addPanelObject(String name, T object) {
        if(object == null)
            return;
        if(objectList.contains(name)) {
            Log.error(this.key + " with name " + name + " already exist!");
            return;
        }
        object.setKey(name);
        panelObjects.put(object.getKey(), object);
        objectList.addElement(object.getKey());
    }

    @SuppressWarnings("unchecked")
    protected T createNewInstance() {
        T instance = null;
        try { instance = (T) panelObject.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) { Log.error("Can not create Panel Object instance!", ex); }
        return instance;
    }

}
