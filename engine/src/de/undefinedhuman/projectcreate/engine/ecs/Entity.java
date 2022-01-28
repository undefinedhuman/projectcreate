package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import de.undefinedhuman.projectcreate.engine.utils.ds.Bag;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

public class Entity {

    public static final long DEFAULT_WORLD_ID = -1L;

    public int flags;

    boolean scheduledForRemoval;

    private long worldID;
    private int blueprintID;
    private Bag<Component> components = new Bag<>(16);
    private Array<Component> componentsArray = new Array<>(false, 16);
    private ImmutableArray<Component> immutableComponents = new ImmutableArray<>(componentsArray);

    private Bits componentBits = new Bits(), familyBits = new Bits();
//    private Observable<Entity> componentObservable = new Observable<>();

    public Entity(int blueprintID) {
        this(blueprintID, DEFAULT_WORLD_ID);
    }

    public Entity(int blueprintID, long worldID) {
        this.blueprintID = blueprintID;
        this.worldID = worldID;
    }

    public Entity add (Component component) {
        Class<? extends Component> componentClass = component.getClass();
        Component oldComponent = getComponent(componentClass);
        if (component == oldComponent)
            return this;
        if (oldComponent != null)
            remove(componentClass);
        int componentTypeIndex = ComponentType.indexOf(componentClass);
        components.set(componentTypeIndex, component);
        componentsArray.add(component);
        componentBits.set(componentTypeIndex);
        // componentObservable.notify(this);
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
            // componentObservable.notify(this);
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

    public void setWorldID(long worldID) {
        this.worldID = worldID;
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

//    public Observable<Entity> getComponentObservable() {
//        return componentObservable;
//    }

    public ImmutableArray<Component> getComponents() {
        return immutableComponents;
    }

}
