package de.undefinedhuman.sandboxgame.entity.ecs.blueprint;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityType;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentParam;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.HashMap;

public class Blueprint {

    private int id;
    private EntityType type;
    private HashMap<ComponentType, ComponentBlueprint> componentBlueprints;
    private Vector2 size;

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
            entity.addComponent(blueprint.createInstance(entity, params));
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
