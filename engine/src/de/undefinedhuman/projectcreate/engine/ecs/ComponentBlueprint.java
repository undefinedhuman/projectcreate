package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.RequiredComponents;
import de.undefinedhuman.projectcreate.engine.ecs.events.ComponentBlueprintEvent;
import de.undefinedhuman.projectcreate.engine.event.EventManager;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.listener.ValueListener;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.util.Locale;

public abstract class ComponentBlueprint<T extends Component> extends SettingsList implements Comparable<ComponentBlueprint> {

    public int blueprintID;

    private Class<? extends ComponentBlueprint>[] requiredComponents;
    private EventManager eventManager;
    private ValueListener valueListener = value -> notifyEventManager();

    protected ComponentPriority priority = ComponentPriority.LOWEST;

    public ComponentBlueprint() {
        RequiredComponents requiredComponents = Metadata.getAnnotation(getClass(), RequiredComponents.class);
        this.requiredComponents = requiredComponents != null ? requiredComponents.value() : null;
    }

    public abstract T createInstance();

    @Override
    protected void addSetting(Setting<?> setting) {
        super.addSetting(setting.addValueListener(valueListener));
    }

    @Override
    protected void removeSetting(Setting<?> setting) {
        super.removeSetting(setting.removeValueListener(valueListener));
    }

    public void load(FileHandle parentDir, SettingsObject settingsObject) {
        for(Setting<?> setting : this.getSettings())
            setting.load(parentDir, settingsObject);
    }

    public void save(FileWriter writer) {
        writer.writeString("{:" + getName(getClass())).nextLine();
        Utils.saveSettings(writer, this);
        writer.writeString("}").nextLine();
    }

    public void validate() {
        for(Setting<?> setting : this.getSettings())
            setting.validate();
    }

    public ComponentPriority getPriority() {
        return priority;
    }

    public Class<? extends ComponentBlueprint>[] getRequiredComponents() {
        return requiredComponents;
    }

    public ComponentBlueprint<?> setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

    private void notifyEventManager() {
        if(eventManager == null)
            return;
        eventManager.notify(new ComponentBlueprintEvent(ComponentBlueprintEvent.Type.UPDATE, this));
    }

    @Override
    public int compareTo(ComponentBlueprint o) {
        int priority = getPriority().compareTo(o.getPriority());
        if(priority == 0)
            priority = toString().toLowerCase().compareTo(o.toString().toLowerCase());
        return priority;
    }

    @Override
    public String toString() {
        return getName(getClass());
    }

    public static String getName(Class<? extends ComponentBlueprint> componentBlueprint) {
        String name = componentBlueprint.getSimpleName().split("Blueprint")[0];
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1).toLowerCase();
    }

}
