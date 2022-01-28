package de.undefinedhuman.projectcreate.engine.ecs;

import java.util.HashMap;

public class Blueprint {

    private HashMap<Class<? extends ComponentBlueprint>, ComponentBlueprint> componentBlueprints;
    private int blueprintID;

    public Blueprint(int id) {
        componentBlueprints = new HashMap<>();
        this.blueprintID = id;
    }

    public Entity createInstance(long worldID) {
        Entity entity = new Entity(blueprintID, worldID);
        for (ComponentBlueprint componentBlueprint : componentBlueprints.values())
            entity.add(componentBlueprint.createInstance());
        return entity;
    }

    public int getBlueprintID() {
        return blueprintID;
    }

    public void addComponentBlueprint(ComponentBlueprint blueprint) {
        componentBlueprints.putIfAbsent(blueprint.getClass(), blueprint);
    }

    public <T extends ComponentBlueprint> T getComponentBlueprint(Class<T> type) {
        return hasComponentBlueprints(type) ? (T) componentBlueprints.get(type) : null;
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

    public void validate() {
        componentBlueprints.values().forEach(ComponentBlueprint::validate);
    }

    public void delete() {
        for (ComponentBlueprint blueprint : componentBlueprints.values())
            blueprint.delete();
    }

}
