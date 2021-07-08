package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

public abstract class Panel<T extends PanelObject> extends Setting<String> {

    protected static final int INPUT_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT;
    private static final int BUTTON_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT;
    private static final int OBJECT_PANEL_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT*4;

    private JList<String> objectSelectionList;

    protected HashMap<String, T> panelObjects = new HashMap<>();
    protected DefaultListModel<String> objectList;
    protected T panelObject;

    private JPanel objectPanel;

    public Panel(String key, T panelObject) {
        super(key, "");
        this.panelObject = panelObject;
        setContentHeight(INPUT_HEIGHT + BUTTON_HEIGHT + OBJECT_PANEL_HEIGHT + BUTTON_HEIGHT + Variables.OFFSET*4 + getPanelObjectContentHeight());
    }

    @Override
    protected void delete() {
        super.delete();
        for(PanelObject animation : panelObjects.values()) animation.delete();
        panelObjects.clear();
    }

    @Override
    protected void createValueUI(JPanel panel, int width) {
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
        objectPanel.setBounds(0, INPUT_HEIGHT + BUTTON_HEIGHT + OBJECT_PANEL_HEIGHT + BUTTON_HEIGHT + Variables.OFFSET*4, width, getPanelObjectContentHeight());
        objectPanel.setOpaque(true);

        createPanelObjectNameComponent(panel, width);
        panel.add(createButton("Add",
                0,
                (float) INPUT_HEIGHT + Variables.OFFSET,
                width/2f - Variables.OFFSET/2f,
                e -> {
                    String name = getSelectedObjectName();
                    if(name.equalsIgnoreCase(""))
                        return;
                    addPanelObject(name);
                }
        ));
        panel.add(createButton("Remove",
                width/2f + Variables.OFFSET/2f,
                (float) INPUT_HEIGHT + Variables.OFFSET,
                width/2f - Variables.OFFSET/2f,
                e -> removePanelObject(getSelectedObjectName())));
        panel.add(objectSelectionPanel);
        createUtilityButtons(panel, INPUT_HEIGHT + BUTTON_HEIGHT + OBJECT_PANEL_HEIGHT + Variables.OFFSET*3, width);
        panel.add(objectPanel);
    }

    @Override
    protected void updateMenu(String value) {}

    @SuppressWarnings("unchecked")
    @Override
    protected void loadValue(FileHandle parentDir, Object value) {
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
            panelObjects.put(key, (T) panelObject.setKey(key).load(parentDir, (SettingsObject) panelObjectSettings));
        }
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        for(PanelObject object : this.panelObjects.values())
            object.save(writer);
        writer.writeString("}");
    }

    @Override
    protected void saveValue(FileWriter writer) {}

    private int getPanelObjectContentHeight() {
        int height = 0;
        for(Setting<?> setting : panelObject.getSettings())
            height += setting.getTotalHeight();
        return height;
    }

    public HashMap<String, T> getPanelObjects() {
        return panelObjects;
    }

    public Collection<T> values() {
        return panelObjects.values();
    }

    protected abstract void createPanelObjectNameComponent(JPanel panel, int width);
    protected abstract String getSelectedObjectName();
    protected abstract void selectObject(T object, JPanel panel, int containerWidth);

    protected void createUtilityButtons(JPanel panel, int y, int width) {
        panel.add(createButton("Clear All",
                width/3f*2f + Variables.OFFSET/2f,
                y,
                width/3f - Variables.OFFSET/2f,
                e -> removeAllPanelObjects()));
    }

    protected void addPanelObject(String name) {
        addPanelObject(name, createNewInstance());
    }

    protected void addPanelObject(String name, T panelObject) {
        if(panelObject == null)
            return;
        if(objectList.contains(name)) {
            Log.error(this.key + " with name " + name + " already exist!");
            return;
        }
        panelObject.setKey(key);
        panelObjects.put(panelObject.getKey(), panelObject);
        objectList.addElement(panelObject.getKey());
    }

    private void removePanelObject(String name) {
        objectList.removeElement(name);
        panelObjects.remove(name);
        Tools.removeSettings(objectPanel);
    }

    private void removeAllPanelObjects() {
        objectList.clear();
        panelObjects.clear();
        Tools.removeSettings(objectPanel);
    }

    @SuppressWarnings("unchecked")
    protected T createNewInstance() {
        T instance = null;
        try { instance = (T) panelObject.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) { Log.error("Can not create Panel Object instance!", ex); }
        return instance;
    }

    protected JButton createButton(String text, float x, float y, float width, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds((int) x, (int) y, (int) width, BUTTON_HEIGHT);
        button.addActionListener(listener);
        return button;
    }

}
