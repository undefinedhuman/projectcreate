package de.undefinedhuman.projectcreate.engine.settings.panels;

import de.undefinedhuman.projectcreate.engine.utils.Tools;

import javax.swing.*;

public class StringPanel<T extends PanelObject> extends Panel<T> {

    private JTextField objectName;

    public StringPanel(String name, T panelObject) {
        super(name, panelObject);
    }

    @Override
    protected void createObjectNameField(JPanel panel, int x, int y, int width, int height) {
        objectName = new JTextField("");
        objectName.setBounds(x, y, width, height);
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
        Tools.addSettings(objectPanel, object.getSettings(), containerWidth);
    }

}
