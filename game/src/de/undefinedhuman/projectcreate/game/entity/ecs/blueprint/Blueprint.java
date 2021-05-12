package de.undefinedhuman.projectcreate.game.entity.ecs.blueprint;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.entity.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.ComponentParam;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.entity.EntityType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.game.entity.Entity;

import java.util.HashMap;

public class Blueprint {

    public SettingsList settings = new SettingsList();

    public Setting
            id = new Setting(SettingType.Int, "ID", 0),
            name = new Setting(SettingType.String, "Name", ""),
            size = new Vector2Setting("Size", new Vector2()),
            type = new SelectionSetting("Type", EntityType.values());

    private HashMap<ComponentType, ComponentBlueprint> componentBlueprints;

    public Blueprint() {
        settings.addSettings(id, name, size, type);
        componentBlueprints = new HashMap<>();
    }

    public Entity createInstance(ComponentParam... param) {
        Entity entity = new Entity(this, size.getVector2());
        HashMap<ComponentType, ComponentParam> params = new HashMap<>();
        for (ComponentParam p : param) params.put(p.getType(), p);
        for (ComponentBlueprint blueprint : componentBlueprints.values())
            entity.addComponents(blueprint.createInstance(params));
        entity.init();
        return entity;
    }

    public int getID() {
        return id.getInt();
    }
    public EntityType getType() {
        return type.getEntityType();
    }

    public void addComponentBlueprint(ComponentBlueprint blueprint) {
        componentBlueprints.putIfAbsent(blueprint.getType(), blueprint);
    }

    public ComponentBlueprint getComponentBlueprint(ComponentType type) {
        if(!hasComponentBlueprints(type)) return null;
        return componentBlueprints.get(type);
    }

    public boolean hasComponentBlueprints(ComponentType type) {
        return componentBlueprints.containsKey(type);
    }

    public HashMap<ComponentType, ComponentBlueprint> getComponentBlueprints() {
        return componentBlueprints;
    }

    public void delete() {
        for (ComponentBlueprint blueprint : componentBlueprints.values()) blueprint.delete();
    }

}
