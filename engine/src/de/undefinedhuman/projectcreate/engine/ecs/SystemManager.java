package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

class SystemManager extends Manager {

    private final HashMap<Class<? extends System>, System> systems = new HashMap<>();
    private final Collection<System> unmodifiableSystems = Collections.unmodifiableCollection(systems.values());
    private final Array<System> orderedSystems = new Array<>(true, 16);
    private final ImmutableArray<System> orderedUnmodifiableSystems = new ImmutableArray<>(orderedSystems);
    private final SystemComparator systemComparator = new SystemComparator();

    public void addSystem(System system) {
        Class<? extends System> systemType = system.getClass();
        System oldSystem = getSystem(systemType);
        if (oldSystem != null)
            removeSystem(systemType);
        systems.put(systemType, system);
        orderedSystems.add(system);
        orderedSystems.sort(systemComparator);
    }

    public void removeSystem(Class<? extends System> type) {
        System system = systems.get(type);
        if(system == null)
            return;
        systems.remove(type);
        orderedSystems.removeValue(system, true);
    }

    public void removeAllSystems() {
        systems.clear();
        orderedSystems.clear();
    }

    public ImmutableArray<System> getOrderedUnmodifiableSystems() {
        return orderedUnmodifiableSystems;
    }

    public Collection<System> getUnmodifiableSystems() {
        return unmodifiableSystems;
    }

    public <T extends System> T getSystem(Class<T> systemType) {
        return (T) systems.get(systemType);
    }

    static class SystemComparator implements Comparator<System> {
        @Override
        public int compare(System a, System b) {
            return Integer.compare(a.getPriority(), b.getPriority());
        }
    }

}
