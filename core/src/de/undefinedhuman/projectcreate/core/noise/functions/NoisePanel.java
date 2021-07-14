package de.undefinedhuman.projectcreate.core.noise.functions;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class NoisePanel extends Setting<String> {

    private static final int BUTTON_WIDTH = 75;

    private JComboBox<String> selection;
    private Box box;
    private HashMap<JPanel, BaseFunction> guiPanels = new HashMap<>();
    private HashMap<String, Class<? extends BaseFunction>> noiseFunctions = new HashMap<>();

    public NoisePanel(String key, int height, Class<? extends BaseFunction>... functions) {
        super(key, "");
        for(Class<? extends BaseFunction> function : functions)
            noiseFunctions.put(function.getSimpleName(), function);
        setContentHeight(height);
    }

    @Override
    protected void delete() {
        super.delete();
        guiPanels.clear();
    }

    @Override
    public void createSettingUI(Accordion accordion) {

        JPanel panel = new JPanel(null);

        JButton addButton = new JButton("Add");
        addButton.setBounds(400 - BUTTON_WIDTH, 0, BUTTON_WIDTH, Variables.DEFAULT_CONTENT_HEIGHT);
        addButton.addActionListener(e -> {
            BaseFunction function = newInstance((String) selection.getSelectedItem());
            if(function == null)
                return;
            addPanel(function, 400 - Variables.BORDER_WIDTH*2 - Variables.OFFSET);
            for(Setting<?> setting : function.getSettings())
                setting.addValueListener(value -> updateValues());
        });
        panel.add(addButton);

        selection = new JComboBox<>(noiseFunctions.keySet().stream().sorted().map(String::valueOf).toArray(value -> new String[noiseFunctions.size()]));
        selection.setBounds(0, 0, 400 - BUTTON_WIDTH - Variables.OFFSET, Variables.DEFAULT_CONTENT_HEIGHT);
        panel.add(selection);

        box = Box.createVerticalBox();
        DragAdapter dragAdapter = new DragAdapter();
        box.addMouseListener(dragAdapter);
        box.addMouseMotionListener(dragAdapter);
        box.setAutoscrolls(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(box, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setLocation(0, Variables.DEFAULT_CONTENT_HEIGHT + Variables.OFFSET);
        scrollPane.setSize(new Dimension(400, getContentHeight() - Variables.DEFAULT_CONTENT_HEIGHT - Variables.OFFSET));
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setBorder(null);
        panel.add(scrollPane);
        accordion.addCollapsiblePanel(key, panel);
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
            BaseFunction baseFunction = newInstance(key);
            if(baseFunction == null)
                return;
            addPanel(baseFunction, box.getWidth() - Variables.BORDER_WIDTH*2 - Variables.OFFSET);
        }
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        for(PanelObject object : this.guiPanels.values())
            object.save(writer);
        writer.writeString("}");
    }

    @Override
    protected void saveValue(FileWriter writer) {}

    @Override
    protected void updateMenu(String value) {}

    public double calculateValue(int x, int y) {
        double value = 0;
        if(box == null)
            return 0;
        for(int i = 0; i < box.getComponentCount(); i++)
            value = guiPanels.get((JPanel) box.getComponent(i)).calculateValue(x, y, value);
        return value;
    }

    private void addPanel(BaseFunction function, int width) {
        addPanel(function.createPanel(width, this::removePanel), function);
    }

    private void addPanel(JPanel panel, BaseFunction function) {
        guiPanels.put(panel, function);
        box.add(panel);
        updateBox();
    }

    private void removePanel(JPanel panel) {
        guiPanels.remove(panel);
        box.remove(panel);
        updateBox();
    }

    private void updateBox() {
        box.revalidate();
        box.repaint();
    }

    private BaseFunction newInstance(String name) {
        BaseFunction function = null;
        try {
            function = noiseFunctions.get(name).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Error while creating base function instance!", ex);
        }
        return function;
    }

    public abstract void updateValues();

}
