package de.undefinedhuman.projectcreate.engine.observer;

import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;

import java.util.HashMap;
import java.util.List;

public class SynchronizedEventManager extends EventManager {

    private HashMap<Class<? extends Event<?, ?>>, MultiMap<?, ?>> temporaryData = new HashMap<>();

    public <EventType, Datatype> void notify(Class<? extends Event<EventType, Datatype>> eventClass, EventType eventType, Datatype data, boolean delayed) {
        if(delayed) add(eventClass, eventType).add(data);
        else super.notify(eventClass, eventType, data);
    }

    private <EventType, Datatype> List<Datatype> add(Class<? extends Event<EventType, Datatype>> eventClass, EventType eventType) {
        MultiMap<EventType, Datatype> eventMap = (MultiMap<EventType, Datatype>) temporaryData.get(eventClass);
        if(eventMap == null)
            temporaryData.put(eventClass, eventMap = new MultiMap<>());
        List<Datatype> dataList = eventMap.getDataForKey(eventType);
        if(dataList == null)
            dataList = eventMap.add(eventType);
        return dataList;
    }

    public void processTemporaryObserverData() {
        for(Class<? extends Event<?, ?>> eventClass : temporaryData.keySet())
            processMultiMap(eventClass);
    }

    public void clearTemporaryObserverData() {
        for(Class<? extends Event<?, ?>> eventClass : temporaryData.keySet())
            clearMultiMap(eventClass);
    }

    private <EventType, Datatype> MultiMap<EventType, Datatype> getMultiMapFor(Class<? extends Event<?, ?>> eventType) {
        return (MultiMap<EventType, Datatype>) temporaryData.get(eventType);
    }

    private <EventType, Datatype> void processMultiMap(Class<? extends Event<?, ?>> eventClass) {
        MultiMap<EventType, Datatype> map = getMultiMapFor(eventClass);
        map.keySet().forEach(eventType -> map.getDataForKey(eventType).forEach(data -> super.notify((Class<? extends Event<EventType, Datatype>>) eventClass, (EventType) eventType, (Datatype) data)));
    }

    private <EventType, Datatype> void clearMultiMap(Class<? extends Event<?, ?>> eventClass) {
        MultiMap<EventType, Datatype> map = getMultiMapFor(eventClass);
        map.keySet().forEach(eventType -> map.getDataForKey(eventType).clear());
    }

}