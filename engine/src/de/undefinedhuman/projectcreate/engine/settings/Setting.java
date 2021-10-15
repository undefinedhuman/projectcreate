package de.undefinedhuman.projectcreate.engine.settings;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.listener.ValueListener;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;

import java.util.ArrayList;

public abstract class Setting<T> {

    protected String key;
    private String menuTitle;
    protected T value;

    private ArrayList<ValueListener<T>> valueListeners = new ArrayList<>();

    public Setting(String key, T defaultValue) {
        this.key = key;
        this.value = defaultValue;
        setMenuTitle(key);
    }

    public void init() {}

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

    public Setting<T> addValueListener(ValueListener<T> listener) {
        if(!valueListeners.contains(listener))
            valueListeners.add(listener);
        return this;
    }

    public void validate() {}

    protected void delete() { value = null; }

    @Override
    public String toString() {
        return "[" + key + ", " + getValue().toString() + "]";
    }

    protected abstract void loadValue(FileHandle parentDir, Object value);

    protected abstract void saveValue(FileWriter writer);

    public abstract void createSettingUI(Accordion accordion);

    protected abstract void updateMenu(T value);

}
