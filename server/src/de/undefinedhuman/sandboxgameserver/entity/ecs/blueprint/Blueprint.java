package de.undefinedhuman.sandboxgameserver.entity.ecs.blueprint;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.EntityType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;

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

    public Entity createInstance() {

        Entity entity = new Entity(this, size);
        for (ComponentBlueprint blueprint : componentBlueprints.values()) entity.addComponent(blueprint.createInstance(entity));
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
