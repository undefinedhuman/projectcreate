package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.observer.Event;
import de.undefinedhuman.projectcreate.engine.observer.EventManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Blueprint {

    private EventManager eventManager;
    private HashMap<Class<? extends ComponentBlueprint>, ComponentBlueprint> componentBlueprints;
    private int blueprintID;

    public Blueprint(int id) {
        componentBlueprints = new HashMap<>();
        this.blueprintID = id;
    }

    public Blueprint setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

    Entity createInstance(long worldID) {
        if(worldID < 0) {
            Log.error("World ID of entity must be greater or equal to null");
            return null;
        }
        Entity entity = new Entity(blueprintID, worldID);
        for (ComponentBlueprint componentBlueprint : componentBlueprints.values())
            entity.add(componentBlueprint.createInstance());
        return entity;
    }

    public int getBlueprintID() {
        return blueprintID;
    }

    public void addComponentBlueprints(ComponentBlueprint... blueprints) {
        ComponentBlueprint[] componentBlueprintsToAdd = Arrays
                .stream(blueprints)
                .filter(componentBlueprint -> {
                    if(!hasComponentBlueprints(componentBlueprint.getClass()))
                        return true;
                    Log.warn("Blueprint with id: " + getBlueprintID() + " already contains component blueprint: " + componentBlueprint.getClass().getSimpleName());
                    return false;
                })
                .toArray(ComponentBlueprint[]::new);
        for(ComponentBlueprint componentBlueprint : componentBlueprintsToAdd) {
            this.componentBlueprints.put(componentBlueprint.getClass(), componentBlueprint);
            if(eventManager != null) componentBlueprint.setEventManager(eventManager);
        }
        if(eventManager != null) eventManager.notify(ComponentBlueprintEvent.class, ComponentBlueprintEvent.Type.ADD, componentBlueprintsToAdd);
    }

    public <T extends ComponentBlueprint> T getComponentBlueprint(Class<T> type) {
        return hasComponentBlueprints(type) ? (T) componentBlueprints.get(type) : null;
    }

    public boolean hasComponentBlueprints(Class<? extends ComponentBlueprint> type) {
        return componentBlueprints.containsKey(type);
    }

    @SafeVarargs
    public final void removeComponentBlueprints(Class<? extends ComponentBlueprint>... types) {
        ComponentBlueprint[] componentBlueprintsToRemove = Arrays
                .stream(types)
                .map(this::getComponentBlueprint).filter(Objects::nonNull)
                .toArray(ComponentBlueprint[]::new);
        for(ComponentBlueprint componentBlueprint : componentBlueprintsToRemove) {
            this.componentBlueprints.remove(componentBlueprint.getClass());
            componentBlueprint.setEventManager(null);
        }
        if(eventManager != null) eventManager.notify(ComponentBlueprintEvent.class, ComponentBlueprintEvent.Type.REMOVE, componentBlueprintsToRemove);
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

    public static class ComponentBlueprintEvent extends Event<ComponentBlueprintEvent.Type, ComponentBlueprint[]> {

        protected ComponentBlueprintEvent() {
            super(Type.class, ComponentBlueprint[].class);
        }

        public enum Type {
            ADD,
            REMOVE,
            UPDATE
        }
    }

}
