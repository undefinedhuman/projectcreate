package de.undefinedhuman.projectcreate.engine.ecs;

public class ComponentMapper<T extends Component> {
    private final ComponentType componentType;

    public static <T extends Component> ComponentMapper<T> create(Class<T> componentClass) {
        return new ComponentMapper<>(componentClass);
    }

    public T get (Entity entity) {
        return entity.getComponent(componentType);
    }

    public boolean has (Entity entity) {
        return entity.hasComponent(componentType);
    }

    private ComponentMapper (Class<T> componentClass) {
        componentType = ComponentType.createOrReturn(componentClass);
    }
}