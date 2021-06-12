package de.undefinedhuman.projectcreate.engine.settings;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.listener.ValueListener;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;
import java.util.ArrayList;

public abstract class Setting<T> {

    private static final int TITLE_LABEL_HEIGHT = 25;

    protected String key;
    private String menuTitle;
    protected T value;
    private int contentHeight = Variables.DEFAULT_CONTENT_HEIGHT;

    private ArrayList<ValueListener<T>> valueListeners = new ArrayList<>();

    public Setting(String key, T defaultValue) {
        this.key = key;
        this.value = defaultValue;
        setMenuTitle(key);
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getKey() { return key; }
    public T getValue() { return value; }
    public void setValue(T value) {
        this.value = value;
        valueListeners.forEach(valueListener -> valueListener.notify(getValue()));
    }

    protected void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }

    protected int getContentHeight() {
        return contentHeight;
    }

    public int getTotalHeight() {
        return contentHeight + TITLE_LABEL_HEIGHT;
    }

    public void load(FileHandle parentDir, SettingsObject settingsObject) {
        if(!settingsObject.containsKey(key))
            return;
        loadValue(parentDir, settingsObject.get(key));
        updateMenu(getValue());
    }

    protected abstract void loadValue(FileHandle parentDir, Object value);

    public void save(FileWriter writer) {
        writer.writeString(key);
        saveValue(writer);
    }

    protected abstract void saveValue(FileWriter writer);

    public JPanel createMenuComponents(Vector2i position, int containerWidth) {

        JPanel container = new JPanel(null);
        container.setSize(containerWidth, getContentHeight());

        JLabel titleLabel = new JLabel(menuTitle);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(position.x, position.y, containerWidth, TITLE_LABEL_HEIGHT);
        titleLabel.setBackground(Variables.BACKGROUND_COLOR.darker());
        titleLabel.setOpaque(true);
        container.add(titleLabel);

        JPanel contentPanel = new JPanel(null);
        contentPanel.setSize(containerWidth, contentHeight);
        createValueMenuComponents(contentPanel, contentPanel.getWidth());

        JScrollPane valueMenuComponentContainer = new JScrollPane(contentPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        valueMenuComponentContainer.setBorder(null);
        valueMenuComponentContainer.setBounds(position.x, position.y + TITLE_LABEL_HEIGHT, containerWidth, contentHeight);
        container.add(valueMenuComponentContainer);

        return container;

    }

    public Setting<T> addValueListener(ValueListener<T> listener) {
        if(!valueListeners.contains(listener))
            valueListeners.add(listener);
        return this;
    }

    protected void delete() { value = null; }

    @Override
    public String toString() {
        return "[" + key + ", " + getValue().toString() + "]";
    }

    protected abstract void createValueMenuComponents(JPanel panel, int width);

    protected abstract void updateMenu(T value);

}
