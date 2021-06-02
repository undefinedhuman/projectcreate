package de.undefinedhuman.projectcreate.core.noise.functions;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class NoisePanel extends Setting<String> {

    private static final int BUTTON_WIDTH = 75;

    private JComboBox<String> selection;
    private Box box;
    private HashMap<JPanel, BaseFunction> guiPanels = new HashMap<>();
    private HashMap<String, Class<? extends BaseFunction>> noiseFunctions = new HashMap<>();

    public NoisePanel(String key, Class<? extends BaseFunction>... functions) {
        super(key, "", String::valueOf);
        setContentHeight(400);
        for(Class<? extends BaseFunction> function : functions)
            noiseFunctions.put(function.getSimpleName(), function);
    }

    @Override
    protected void delete() {
        super.delete();
        guiPanels.clear();
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, int width) {

        JButton addButton = new JButton("Add");
        addButton.setBounds(width - BUTTON_WIDTH, 0, BUTTON_WIDTH, Variables.DEFAULT_CONTENT_HEIGHT);
        addButton.addActionListener(e -> {
            BaseFunction function = newInstance((String) selection.getSelectedItem());
            if(function == null)
                return;
            addPanel(function, width - Variables.BORDER_WIDTH*2 - Variables.OFFSET);
        });
        panel.add(addButton);

        selection = new JComboBox<>(noiseFunctions.keySet().toArray(new String[0]));
        selection.setBounds(0, 0, width - BUTTON_WIDTH - Variables.OFFSET, Variables.DEFAULT_CONTENT_HEIGHT);
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
        scrollPane.setSize(new Dimension(width, 400 - Variables.DEFAULT_CONTENT_HEIGHT - Variables.OFFSET));
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        panel.add(scrollPane);
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

    private void addPanel(BaseFunction function, int width) {
        JPanel panel = function.createPanel(width - Variables.BORDER_WIDTH*2 - Variables.OFFSET);
        guiPanels.put(panel, function);
        box.add(panel);
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

}
