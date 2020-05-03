package de.undefinedhuman.sandboxgame.entity.ecs.blueprint;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.EntityType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.Vector2Setting;
import de.undefinedhuman.sandboxgame.entity.Entity;

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

    public void delete() {
        for (ComponentBlueprint blueprint : componentBlueprints.values()) blueprint.delete();
    }

}
