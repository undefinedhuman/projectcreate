package de.undefinedhuman.projectcreate.engine.settings.panels;

import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;

import javax.swing.*;
import java.awt.*;

public class StringPanel<T extends PanelObject> extends Panel<T> {

    private JTextField objectName;

    public StringPanel(String name, Class<T> panelObjectClass) {
        super(name, panelObjectClass);
    }

    @Override
    protected void createPanelObjectNameComponent(JPanel panel) {
        objectName = new JTextField("");
        objectName.setFont(objectName.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        objectName.setPreferredSize(new Dimension(0, Panel.INPUT_HEIGHT));
        panel.add(objectName);
    }

    @Override
    public String getSelectedObjectName() {
        return objectName.getText();
    }

    @Override
    public void selectObject(T object, Accordion panel) {
        objectName.setText(object.getKey());
        for(Setting<?> setting : object.getSettings())
            setting.createSettingUI(panel);
    }

    @Override
    protected void removePanelObject(String name) {
        objectName.setText("");
        super.removePanelObject(name);
    }

    @Override
    protected void removeAllPanelObjects() {
        super.removeAllPanelObjects();
        objectName.setText("");
    }
}
