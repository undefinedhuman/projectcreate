package de.undefinedhuman.projectcreate.core.noise.functions;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.panel.AccordionPanel;
import de.undefinedhuman.projectcreate.engine.settings.ui.layout.RelativeLayout;
import de.undefinedhuman.projectcreate.engine.settings.ui.listener.DragAdapter;
import de.undefinedhuman.projectcreate.engine.settings.ui.ui.SettingsUI;
import de.undefinedhuman.projectcreate.engine.settings.ui.utils.SettingsUtils;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public abstract class NoisePanel extends Setting<HashMap<String, Class<? extends BaseFunction>>> {

    private JComboBox<String> selection;
    private Accordion functions;
    private HashMap<AccordionPanel, BaseFunction> functionPanels = new HashMap<>();

    @SafeVarargs
    public NoisePanel(String key, Class<? extends BaseFunction>... functions) {
        super(key, new HashMap<>());
        Arrays.stream(functions).forEach(functionClass -> value.put(functionClass.getSimpleName().split("Function")[0], functionClass));
    }

    @Override
    protected void delete() {
        value.clear();
        functionPanels.clear();
    }

    @Override
    public void createSettingUI(Accordion accordion) {
        JPanel panel = new JPanel(new RelativeLayout(RelativeLayout.Y_AXIS).setFill(true));

        selection = new JComboBox<>(value.keySet().stream().sorted().toArray(String[]::new));
        selection.setPreferredSize(new Dimension(0, Variables.DEFAULT_CONTENT_HEIGHT));
        selection.setFont(selection.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        selection.addActionListener(e -> {
            BaseFunction function = newInstance(value.get((String) selection.getSelectedItem()));
            if(function == null)
                return;
            addPanel(function);
        });
        panel.add(selection);

        functions = SettingsUI.createAccordion(accordion, "", Variables.BACKGROUND_COLOR.brighter(), false);
        SettingsUtils.setDragListener(new DragAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                super.mouseReleased(event);
                updateValues();
            }
        }, functions.getContentPanel());
        panel.add(functions);

        accordion.addCollapsiblePanel(key, panel);
    }

    @Override
    protected void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof SettingsObject)) return;
        SettingsObject settingsObject = (SettingsObject) value;
        HashMap<String, Object> settings = settingsObject.getSettings();
        ArrayList<BaseFunction> baseFunctions = new ArrayList<>();
        for(String key : settings.keySet()) {
            Object panelObjectSettings = settings.get(key);
            if(!(panelObjectSettings instanceof SettingsObject))
                continue;
            BaseFunction baseFunction = newInstance(this.value.get(key));
            if(baseFunction == null)
                return;
            baseFunction.load(parentDir, (SettingsObject) panelObjectSettings);
            baseFunctions.add(baseFunction);
        }
        baseFunctions.stream().sorted(Comparator.comparing(o -> o.priority.getValue())).forEach(this::addPanel);
    }

    @Override
    public void save(FileWriter writer) {
        writer.writeString("{:" + key).nextLine();
        for(int i = 0; i < functions.getComponentCount(); i++) {
            BaseFunction function = functionPanels.get((AccordionPanel) functions.getContentPanel().getComponent(i));
            function.priority.setValue(i);
            function.save(writer);
        }
        writer.writeString("}");
    }

    public double calculateValue(int x, int y) {
        double value = 0;
        if(functions == null)
            return 0;
        for(int i = 0; i < functions.getContentPanel().getComponentCount(); i++)
            value = functionPanels.get((AccordionPanel) functions.getContentPanel().getComponent(i)).calculateValue(x, y, value);
        return value;
    }

    private void addPanel(BaseFunction function) {
        for(Setting<?> setting : function.getSettings())
            setting.addValueListener(value -> updateValues());
        addPanel(function.createPanel(this::removePanel), function);
        updateValues();
    }

    private void addPanel(JPanel content, BaseFunction function) {
        AccordionPanel accordionPanel = functions.addContentPanel(function.getKey(), content);
        functionPanels.put(accordionPanel, function);
    }

    private void removePanel(JPanel content) {
        AccordionPanel accordionPanel = functions.removeContent(content);
        if(accordionPanel == null)
            return;
        functionPanels.remove(accordionPanel);
        updateValues();
    }

    private BaseFunction newInstance(Class<? extends BaseFunction> functionClass) {
        BaseFunction function = null;
        if(functionClass == null) return null;
        try {
            function = functionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Error while creating base function instance!", ex);
        }
        return function;
    }

    public abstract void updateValues();

    @Override
    protected void saveValue(FileWriter writer) {}

    @Override
    protected void updateMenu(HashMap<String, Class<? extends BaseFunction>> value) {}

}
