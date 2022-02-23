package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import de.undefinedhuman.projectcreate.engine.observer.Event;
import de.undefinedhuman.projectcreate.engine.observer.EventManager;
import de.undefinedhuman.projectcreate.engine.utils.ds.Bag;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

public class Entity {

    public static final long UNDEFINED_WORLD_ID = -1L;
    public static final int UNDEFINED_BLUEPRINT_ID = -1;

    public int flags;

    boolean scheduledForRemoval;

    private EventManager eventManager;
    private long worldID;
    private int blueprintID;
    private Bag<Component> components = new Bag<>(16);
    private Array<Component> componentsArray = new Array<>(false, 16);
    private ImmutableArray<Component> immutableComponents = new ImmutableArray<>(componentsArray);

    private Bits componentBits = new Bits(), familyBits = new Bits();

    Entity(int blueprintID, long worldID) {
        this.blueprintID = blueprintID;
        this.worldID = worldID;
    }

    public Entity add(Component... components) {
        for(Component component : components) {
            Class<? extends Component> componentClass = component.getClass();
            Component oldComponent = getComponent(componentClass);
            if (component == oldComponent)
                return this;
            if (oldComponent != null)
                remove(componentClass);
            int componentTypeIndex = ComponentType.indexOf(componentClass);
            this.components.set(componentTypeIndex, component);
            componentsArray.add(component);
            componentBits.set(componentTypeIndex);
            if(eventManager != null)
                eventManager.notify(ComponentEvent.class, ComponentEvent.Type.COMPONENT, this);
        }
        return this;
    }

    public <T extends Component> T remove (Class<T> componentClass) {
        ComponentType componentType = ComponentType.createOrReturn(componentClass);
        int componentTypeIndex = componentType.getIndex();
        if(componentTypeIndex < components.size()) {
            Component removeComponent = components.get(componentTypeIndex);
            if(removeComponent == null || !hasComponent(componentType))
                return null;
            components.set(componentTypeIndex, null);
            componentsArray.removeValue(removeComponent, true);
            componentBits.clear(componentTypeIndex);
            if(eventManager != null)
                eventManager.notify(ComponentEvent.class, ComponentEvent.Type.COMPONENT, this);
            return (T) removeComponent;
        }
        return null;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return getComponent(ComponentType.createOrReturn(componentClass));
    }

    <T extends Component> T getComponent (ComponentType componentType) {
        return componentType.getIndex() < components.size() ? (T) components.get(componentType.getIndex()) : null;
    }

    boolean hasComponent (ComponentType componentType) {
        return componentBits.get(componentType.getIndex());
    }

    public boolean isScheduledForRemoval() {
        return scheduledForRemoval;
    }

    public long getWorldID() {
        return worldID;
    }

    public Bits getComponentBits() {
        return componentBits;
    }

    public Bits getFamilyBits() {
        return familyBits;
    }

    public int getBlueprintID() {
        return blueprintID;
    }

    public ImmutableArray<Component> getComponents() {
        return immutableComponents;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    static class ComponentEvent extends Event<ComponentEvent.Type, Entity> {
        protected ComponentEvent() {
            super(Type.class, Entity.class);
        }
        enum Type {
            COMPONENT
        }
    }

}
