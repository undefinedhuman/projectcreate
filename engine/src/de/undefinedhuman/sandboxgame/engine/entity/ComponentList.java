package de.undefinedhuman.sandboxgame.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentList {

    private Map<ComponentType, Component> components;

    public ComponentList() {
        components = new HashMap<>();
    }

    public void addComponent(Component component) {
        components.putIfAbsent(component.getType(), component);
    }

    public void removeComponent(ComponentType type) {
        if (hasComponent(type)) components.remove(type);
    }

    public boolean hasComponent(ComponentType type) {
        return components.containsKey(type);
    }

    public Component getComponent(ComponentType type) {
        if (hasComponent(type)) return components.get(type);
        return null;
    }

    public ArrayList<Component> getComponents() {
        return new ArrayList<>(components.values());
    }

    public void delete() {
        components.clear();
    }

}
