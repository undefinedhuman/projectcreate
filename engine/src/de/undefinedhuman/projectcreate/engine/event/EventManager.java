package de.undefinedhuman.projectcreate.engine.event;

import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;

public class EventManager {

    private final MultiMap<Class<? extends Event>, Observer<? extends Event>> observer = new MultiMap<>();

    public <E extends Event> void subscribe(Class<E> eventType, Observer<E> observer) {
        if(this.observer.hasValue(eventType, observer)) return;
        this.observer.add(eventType, observer);
    }

    public <E extends Event> void unsubscribe(Class<E> eventType, Observer<E> observer) {
        if(!this.observer.hasValue(eventType, observer)) return;
        this.observer.removeValue(eventType, observer);
    }

    public <E extends Event> void notify(E event) {
        this.observer.getDataForKey(event.getClass())
                .forEach(observer -> ((Observer<E>) observer).notify(event));
    }

}
