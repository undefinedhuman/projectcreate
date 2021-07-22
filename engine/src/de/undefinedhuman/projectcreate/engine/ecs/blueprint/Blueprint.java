package de.undefinedhuman.projectcreate.engine.ecs.blueprint;

import com.badlogic.ashley.core.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;

import java.util.HashMap;

public class Blueprint {

    private HashMap<Class<? extends ComponentBlueprint>, ComponentBlueprint> componentBlueprints;

    public Blueprint() {
        componentBlueprints = new HashMap<>();
    }

    public Entity createInstance() {
        Entity entity = new Entity();
        for (ComponentBlueprint blueprint : componentBlueprints.values())
            entity.add(blueprint.createInstance());
        return entity;
    }

    public void addComponentBlueprint(ComponentBlueprint blueprint) {
        componentBlueprints.putIfAbsent(blueprint.getClass(), blueprint);
    }

    public ComponentBlueprint getComponentBlueprint(Class<? extends ComponentBlueprint> type) {
        return hasComponentBlueprints(type) ? componentBlueprints.get(type) : null;
    }

    public boolean hasComponentBlueprints(Class<? extends ComponentBlueprint> type) {
        return componentBlueprints.containsKey(type);
    }

    public void removeComponentBlueprint(Class<? extends ComponentBlueprint> type) {
        if(!componentBlueprints.containsKey(type))
            return;
        componentBlueprints.remove(type);
    }

    public HashMap<Class<? extends ComponentBlueprint>, ComponentBlueprint> getComponentBlueprints() {
        return componentBlueprints;
    }

    public void delete() {
        for (ComponentBlueprint blueprint : componentBlueprints.values())
            blueprint.delete();
    }

}
