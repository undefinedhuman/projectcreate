package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Bits;

import java.util.HashMap;

public final class ComponentType {

    private static final HashMap<Class<? extends Component>, ComponentType> ASSIGNED_COMPONENT_TYPES = new HashMap<>();
    private static int GLOBAL_TYPE_INDEX = 0;

    private final int index;

    private ComponentType () {
        index = GLOBAL_TYPE_INDEX++;
    }

    public int getIndex () {
        return index;
    }

    public static ComponentType createOrReturn(Class<? extends Component> componentType) {
        ComponentType type = ASSIGNED_COMPONENT_TYPES.get(componentType);
        if(type != null)
            return type;
        ASSIGNED_COMPONENT_TYPES.put(componentType, type = new ComponentType());
        return type;
    }

    public static int indexOf(Class<? extends Component> componentType) {
        return createOrReturn(componentType).getIndex();
    }

    public static Bits getBitsFor(Class<? extends Component>... componentTypes) {
        Bits bits = new Bits();
        for (Class<? extends Component> componentType : componentTypes)
            bits.set(ComponentType.indexOf(componentType));
        return bits;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ComponentType other = (ComponentType) obj;
        return index == other.index;
    }
}
