package de.undefinedhuman.projectcreate.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentList {

    private Map<Class<? extends Component>, Component> components;

    public ComponentList() {
        components = new HashMap<>();
    }

    public void addComponent(Component component) {
        components.putIfAbsent(component.getClass(), component);
    }

    public void removeComponent(Class<? extends Component> type) {
        if (hasComponent(type)) components.remove(type);
    }

    public boolean hasComponent(Class<? extends Component> type) {
        return components.containsKey(type);
    }

    public Component getComponent(Class<? extends Component> type) {
        if (hasComponent(type)) return components.get(type);
        return null;
    }

    public ArrayList<Component> getComponents() {
        return new ArrayList<>(components.values());
    }

    public void delete() {
        for(Component component : components.values())
            component.delete();
        components.clear();
    }

}
