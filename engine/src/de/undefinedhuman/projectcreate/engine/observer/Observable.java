package de.undefinedhuman.projectcreate.engine.observer;

import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;

public class Observable<T, E> {

    private MultiMap<T, Observer<E>> events = new MultiMap<>();

    public void subscribe(T eventType, Observer<E> event) {
        if(eventType == null || event == null) return;
        this.events.add(eventType, event);
    }

    public void unsubscribe(T eventType, Observer<E> event) {
        if(eventType == null || event == null) return;
        this.events.removeValue(eventType, event);
    }

    public void notify(T eventType, E data) {
        for(Observer<E> observer : events.getValuesForKey(eventType))
            observer.notify(data);
    }

    public boolean hasEvent(T eventType, Observer<E> event) {
        return events.hasValue(eventType, event);
    }

}
