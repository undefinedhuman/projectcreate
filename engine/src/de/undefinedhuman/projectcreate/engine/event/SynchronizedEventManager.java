package de.undefinedhuman.projectcreate.engine.event;

import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;

public class SynchronizedEventManager extends EventManager {

    private final MultiMap<Class<? extends Event>, Event> eventQueue = new MultiMap<>();

    public <E extends Event> void notify(E event, boolean delayed) {
        if(delayed) eventQueue.add(event.getClass(), event);
        else super.notify(event);
    }

    public void processEventQueue() {
        eventQueue.keySet()
                .forEach(eventClass -> {
                    eventQueue.getDataForKey(eventClass)
                            .forEach(super::notify);
                    eventQueue.clearKey(eventClass);
                });
    }

}
