package de.undefinedhuman.projectcreate.engine.observer;

import java.util.HashMap;

public class EventManager {

    private HashMap<Class<? extends Event<?, ?>>, Observable<?, ?>> observables = new HashMap<>();

    public <EventType, DataType> void subscribe(Class<? extends Event<EventType, DataType>> eventClass, EventType eventType, Observer<DataType> observer) {
        if(eventClass == null || eventType == null || observer == null) return;
        Observable<EventType, DataType> observable = addObservable(eventClass);
        observable.subscribe(eventType, observer);
    }

    public <EventType, DataType> void unsubscribe(Class<? extends Event<EventType, DataType>> eventClass, EventType eventType, Observer<DataType> observer) {
        if(eventClass == null || eventType == null || observer == null || !hasObservable(eventClass)) return;
        Observable<EventType, DataType> observable = getObservable(eventClass);
        observable.unsubscribe(eventType, observer);
    }

    public <EventType, DataType> void notify(Class<? extends Event<EventType, DataType>> eventClass, EventType eventType, DataType data) {
        if(eventClass == null || eventType == null || data == null) return;
        Observable<EventType, DataType> observable = getObservable(eventClass);
        if(observable == null) return;
        observable.notify(eventType, data);
    }

    private <EventType, DataType> Observable<EventType, DataType> addObservable(Class<? extends Event<EventType, DataType>> eventClass) {
        Observable<EventType, DataType> observable = getObservable(eventClass);
        if(observable == null) {
            observable = new Observable<>();
            observables.put(eventClass, observable);
        }
        return observable;
    }

    private <EventType, DataType> Observable<EventType, DataType> getObservable(Class<? extends Event<EventType, DataType>> eventClass) {
        return (Observable<EventType, DataType>) observables.get(eventClass);
    }

    private <EventType, DataType> boolean hasObservable(Class<? extends Event<EventType, DataType>> eventClass) {
        return observables.containsKey(eventClass);
    }

}
