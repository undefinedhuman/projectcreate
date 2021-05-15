package de.undefinedhuman.projectcreate.engine.settings;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Getter;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Setting<T> {

    protected String key;
    private String menuTitle;
    protected Object value;
    private int contentHeight = Variables.DEFAULT_CONTENT_HEIGHT;

    protected Getter<T> getter;

    public JTextField valueField;

    public Setting(String key, Object defaultValue, Getter<T> getter) {
        this.key = key;
        this.value = defaultValue;
        this.getter = getter;
        setMenuTitle(key + ":");
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getKey() { return key; }
    public T getValue() { return getter.get(value); }
    public void setValue(Object value) { this.value = value; }

    public int getTotalHeight() {
        return contentHeight + Variables.BORDER_HEIGHT;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    protected void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }

    public void loadSetting(FileHandle parentDir, SettingsObject settingsObject) {
        if(!settingsObject.containsKey(key))
            return;
        loadValue(parentDir, settingsObject.get(key));
        setValueInMenu(value);
    }

    public void save(FileWriter writer) {
        writer.writeString(key);
        writer.writeString(value.toString());
    }

    protected void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        setValue(((LineSplitter) value).getNextString());
    }

    public void addMenuComponents(JComponent container, Vector2 position, int containerWidth) {
        int contentWidth = containerWidth - Variables.BORDER_WIDTH;
        JPanel contentPanel = new JPanel(null);
        contentPanel.setSize(contentWidth, contentHeight);

        addValueMenuComponents(contentPanel, contentWidth);

        JScrollPane settingsContainer = new JScrollPane(contentPanel);
        settingsContainer.setBounds((int) position.x, (int) position.y, containerWidth, contentHeight + Variables.BORDER_HEIGHT);
        settingsContainer.setBorder(BorderFactory.createTitledBorder(menuTitle));
        container.add(settingsContainer);
    }

    protected void addValueMenuComponents(JPanel panel, int width) {
        valueField = createTextField(value, new Vector2(0, 0), new Vector2(width, contentHeight), new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(valueField.getText() == null || valueField.getText().equalsIgnoreCase(""))
                    return;
                setValue(valueField.getText());
            }
        });
        panel.add(valueField);
    }

    protected JTextField createTextField(Object value, Vector2 position, Vector2 size, KeyAdapter adapter) {
        JTextField textField = new JTextField(String.valueOf(value));
        textField.setBounds((int) position.x, (int) position.y, (int) size.x, (int) size.y);
        textField.addKeyListener(adapter);
        return textField;
    }

    protected void setValueInMenu(Object value) {
        if(valueField != null)
            valueField.setText(value.toString());
    }

    protected void delete() { value = null; }

    @Override
    public String toString() {
        return "[" + key + ", " + value + "]";
    }
}
