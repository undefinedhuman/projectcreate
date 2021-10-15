package de.undefinedhuman.projectcreate.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Utils;

import java.util.Locale;

public abstract class ComponentBlueprint extends SettingsList implements Comparable<ComponentBlueprint> {

    public int blueprintID;

    protected ComponentPriority priority = ComponentPriority.LOWEST;

    public abstract Component createInstance();

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
