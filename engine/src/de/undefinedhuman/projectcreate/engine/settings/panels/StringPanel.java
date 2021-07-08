package de.undefinedhuman.projectcreate.engine.settings.panels;

import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;

public class StringPanel<T extends PanelObject> extends Panel<T> {

    private JTextField objectName;

    public StringPanel(String name, T panelObject) {
        super(name, panelObject);
    }

    @Override
    protected void createPanelObjectNameComponent(JPanel panel, int width) {
        objectName = new JTextField("");
        objectName.setBounds(0, 0, width, Panel.INPUT_HEIGHT);
        panel.add(objectName);
    }

    @Override
    public String getSelectedObjectName() {
        return objectName.getText();
    }

    @Override
    public void selectObject(T object, JPanel objectPanel, int containerWidth) {
        objectName.setText(object.getKey());
        Tools.removeSettings(objectPanel);
        Tools.createSettingsPanel(0, containerWidth, object.getSettings().stream());
    }

}
