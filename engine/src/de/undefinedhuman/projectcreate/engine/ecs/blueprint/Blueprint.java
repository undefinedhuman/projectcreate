package de.undefinedhuman.projectcreate.engine.ecs.blueprint;

import com.badlogic.ashley.core.Entity;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;

import java.util.HashMap;

public class Blueprint {

    private HashMap<Class<? extends ComponentBlueprint>, ComponentBlueprint> componentBlueprints;
    private int blueprintID;

    public Blueprint(int id) {
        componentBlueprints = new HashMap<>();
        this.blueprintID = id;
    }

    /*public Entity createInstance() {
        return this.createInstance(IDComponent.UNDEFINED);
    }*/

    public Entity createInstance(long worldID) {
        Entity entity = new Entity();
        entity.add(new IDComponent(worldID, blueprintID));
        for (ComponentBlueprint componentBlueprint : componentBlueprints.values())
            entity.add(componentBlueprint.createInstance());
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
