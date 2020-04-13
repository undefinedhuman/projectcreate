package de.undefinedhuman.sandboxgame.entity.ecs.blueprint;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentParam;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityType;

import java.util.HashMap;

public class Blueprint {

    private Setting
            id = new Setting(SettingType.Int, "ID", 0),
            type = new SelectionSetting("Type", EntityType.values());
    private EntityType type;
    private HashMap<ComponentType, ComponentBlueprint> componentBlueprints;
    private Vector2 size;

            entitySettings.addSettings(
                    new Setting(SettingType.Int, "ID", 0),
                new Setting(SettingType.String, "Name", "Temp Name"),
                new Vector2Setting("Size", new Vector2(0, 0)),
            new SelectionSetting("Type", EntityType.values()));

    public Blueprint(int id, EntityType type, Vector2 size) {
        this.id = id;
        this.type = type;
        this.size = size;
        componentBlueprints = new HashMap<>();
    }

    public Entity createInstance(ComponentParam... param) {
        Entity entity = new Entity(this, size);
        HashMap<ComponentType, ComponentParam> params = new HashMap<>();
        for (ComponentParam p : param) params.put(p.getType(), p);
        for (ComponentBlueprint blueprint : componentBlueprints.values())
            entity.addComponent(blueprint.createInstance(params));
        entity.init();
        return entity;
    }

    public int getID() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public void addComponentBlueprint(ComponentBlueprint blueprint) {
        componentBlueprints.putIfAbsent(blueprint.getType(), blueprint);
    }

    public boolean hasComponent(ComponentType type) {
        return componentBlueprints.containsKey(type);
    }

    public ComponentBlueprint getComponentBlueprint(ComponentType type) {
        return componentBlueprints.get(type);
    }

    public void delete() {
        for (ComponentBlueprint blueprint : componentBlueprints.values()) blueprint.delete();
    }

}