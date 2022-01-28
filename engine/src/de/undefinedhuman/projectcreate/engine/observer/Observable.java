package de.undefinedhuman.projectcreate.engine.observer;

import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;

public class Observable<T extends Enum, E> {

    private MultiMap<T, Observer<E>> events = new MultiMap<>();

    public Observable<T, E> subscribe(T eventType, Observer<E> event) {
        if(eventType == null || event == null) return this;
        this.events.add(eventType, event);
        return this;
    }

    public Observable<T, E> unsubscribe(T eventType, Observer<E> event) {
        if(eventType == null || event == null) return this;
        this.events.removeValue(eventType, event);
        return this;
    }

    public void notify(T eventType, E data) {
        for(Observer<E> observer : events.getValuesForKey(eventType))
            observer.notify(data);
    }

    public boolean hasEvent(T eventType, Observer<E> event) {
        return events.hasValue(eventType, event);
    }

}
