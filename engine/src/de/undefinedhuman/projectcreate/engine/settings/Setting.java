package de.undefinedhuman.projectcreate.engine.settings;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.listener.ValueListener;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

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

    protected void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

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

    public void save(FileWriter writer) {
        writer.writeString(key);
        saveValue(writer);
    }

    public JPanel createSettingUI(int containerWidth) {
        JPanel contentPanel = new JPanel(null);
        contentPanel.setSize(containerWidth, getContentHeight());
        createValueUI(contentPanel, contentPanel.getWidth());
        return contentPanel;
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

    protected abstract void loadValue(FileHandle parentDir, Object value);

    protected abstract void saveValue(FileWriter writer);

    protected abstract void createValueUI(JPanel panel, int width);

    protected abstract void updateMenu(T value);

}
