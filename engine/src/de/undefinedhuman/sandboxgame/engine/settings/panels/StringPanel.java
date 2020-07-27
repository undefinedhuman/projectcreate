package de.undefinedhuman.sandboxgame.engine.settings.panels;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.utils.Tools;

import javax.swing.*;

public class StringPanel extends Panel {

    private JTextField objectName;

    public StringPanel(String name, PanelObject panelObject) {
        super(name, panelObject);
    }

    @Override
    protected void addValueMenuComponents(JPanel panel, Vector2 position) {
        super.addValueMenuComponents(panel, position);
        objectName = new JTextField("");
        objectName.setBounds((int) position.x - 175, (int) position.y + 30, 370, 25);
        panel.add(objectName);
    }

    @Override
    public void addObject() {
        if(objectList.contains(objectName.getText())) {
            Log.error(this.key + " with name " + objectName.getText() + " already exist!");
            return;
        }
        PanelObject object = null;
        try {
            object = this.panelObject.getClass().newInstance();
            object.setKey(objectName.getText());
            objects.put(object.getKey(), object);
        } catch (InstantiationException | IllegalAccessException ex) { ex.printStackTrace(); }
        if(object != null) objectList.addElement(object.getKey());
    }

    @Override
    public void removeObject() {
        if(!objectList.contains(objectName.getText())) {
            Log.error(this.key + " with name " + objectName.getText() + " does not exist!");
            return;
        }
        objectList.removeElement(objectName.getText());
        objects.remove(objectName.getText());
    }

    @Override
    public void selectObject(PanelObject object) {
        objectName.setText(object.getKey());
        Tools.removeSettings(objectPanel);
        Tools.addSettings(objectPanel, object.getSettings());
    }

}
