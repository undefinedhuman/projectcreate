package de.undefinedhuman.projectcreate.engine.settings.panels;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class Panel<K extends Comparable<K>, T extends PanelObject<K>> extends Setting<HashMap<K, T>> {

    protected static final int INPUT_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT;
    protected static final int BUTTON_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT;
    protected static final int OBJECT_PANEL_HEIGHT = Variables.DEFAULT_CONTENT_HEIGHT*6;

    protected DefaultListModel<T> listModel;
    protected Class<T> panelObjectClass;

    private JList<T> panelObjectList;
    private Accordion objectPanel;

    public Panel(String settingKey, Class<T> panelObjectClass) {
        super(settingKey, new HashMap<>());
        this.panelObjectClass = panelObjectClass;
    }

    @Override
    public void init() {
        panelObjectList.setSelectedIndex(0);
    }

    @Override
    protected void delete() {
        value.values().forEach(PanelObject::delete);
        value.clear();
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));

        objectPanel = new Accordion(accordion.getBackgroundColor(), false) {
            @Override
            public void update() {
                super.update();
                accordion.update();
            }
        };

        createPanelObjectNameComponent(panel);

        JPanel menuButtonPanel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true));
        createMenuButtons(menuButtonPanel);
        panel.add(menuButtonPanel);

        listModel = new DefaultListModel<>();
        value.values().stream().sorted().forEach(listModel::addElement);

        panelObjectList = new JList<>(listModel);
        panelObjectList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(c instanceof JLabel && value != null)
                    ((JLabel) c).setText(((PanelObject<K>) value).getDisplayText());
                c.setBackground(index % 2 == 0 ? c.getBackground() : c.getBackground().darker());
                return c;
            }
        });
        panelObjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelObjectList.addListSelectionListener(e -> {
            if(panelObjectList.getSelectedValue() == null)
                return;
            objectPanel.removeAll();
            selectObject(panelObjectList.getSelectedValue(), objectPanel);
        });
        panelObjectList.setFont(panelObjectList.getFont().deriveFont(16f).deriveFont(Font.BOLD));

        JScrollPane objectSelectionPanel = new JScrollPane(panelObjectList);
        objectSelectionPanel.setMinimumSize(new Dimension(0, OBJECT_PANEL_HEIGHT));
        objectSelectionPanel.setPreferredSize(new Dimension(0, OBJECT_PANEL_HEIGHT));
        objectSelectionPanel.setMaximumSize(new Dimension(0, OBJECT_PANEL_HEIGHT));
        panel.add(objectSelectionPanel);

        JPanel utilityButtonPanel = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS).setFill(true));
        createUtilityButtons(utilityButtonPanel, 1f);
        panel.add(utilityButtonPanel);

        panel.add(objectPanel);
        accordion.addContentPanel(key, panel);
        init();
    }

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
            K k = parseKey(key);
            this.value.put(k, (T) panelObject.setKey(k).load(parentDir, (SettingsObject) panelObjectSettings));
        }
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        value.values().forEach(panelObject -> panelObject.save(writer));
        writer.writeString("}");
    }

    protected abstract void createPanelObjectNameComponent(JPanel panel);
    protected abstract K getSelectedKey();
    protected abstract void selectObject(T object, Accordion panel);

    protected void createMenuButtons(JPanel panel) {
        panel.add(SettingsUI.createButton("Add",
                BUTTON_HEIGHT,
                e -> addPanelObject(getSelectedKey())
        ), 0.5f);
        panel.add(SettingsUI.createButton("Remove", BUTTON_HEIGHT, e -> removePanelObject(getSelectedKey())), 0.5f);
    }

    protected void createUtilityButtons(JPanel panel, float remainingWidth) {
        panel.add(SettingsUI.createButton("Clear All", BUTTON_HEIGHT, e -> removeAllPanelObjects()), remainingWidth);
    }

    protected void addPanelObject(K key) {
        if(key == null)
            return;
        addPanelObject(key, createNewInstance());
    }

    protected void addPanelObject(K key, T panelObject) {
        if(panelObject == null)
            return;
        if(value.containsKey(key)) {
            Log.error(this.key + " with name " + key + " already exist!");
            return;
        }
        panelObject.setKey(key);
        value.put(key, panelObject);
        listModel.removeAllElements();
        value.values().stream().sorted().forEach(listModel::addElement);
        panelObjectList.setSelectedValue(panelObject, true);
    }

    protected void removePanelObject(K key) {
        if(key == null)
            return;
        int selectedIndex = panelObjectList.getSelectedIndex();
        listModel.removeElement(value.get(key));
        value.remove(key);
        objectPanel.removeAll();
        panelObjectList.setSelectedIndex(selectedIndex == listModel.size() ? selectedIndex-1 : selectedIndex);
    }

    protected void removeAllPanelObjects() {
        listModel.clear();
        value.clear();
        objectPanel.removeAll();
    }

    protected T createNewInstance() {
        T instance = null;
        try { instance = panelObjectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) { Log.error("Can not create Panel Object instance!", ex); }
        return instance;
    }

    protected abstract K parseKey(String key);

    @Override
    protected void updateMenu(HashMap<K, T> value) {}

    @Override
    protected void saveValue(FileWriter writer) {}

}
