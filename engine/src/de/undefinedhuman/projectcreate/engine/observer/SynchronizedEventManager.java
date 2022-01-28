package de.undefinedhuman.projectcreate.engine.observer;

import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;

import java.util.ArrayList;
import java.util.HashMap;

public class SynchronizedEventManager extends EventManager {

    private HashMap<Class<? extends Event<?, ?>>, MultiMap<?, ?>> temporaryData = new HashMap<>();

    public <EventType extends Enum, Datatype> void notify(Class<? extends Event<EventType, Datatype>> eventClass, EventType eventType, Datatype data, boolean delayed) {
/*        ArrayList<EventType> list = (ArrayList<EventType>) temporaryData.getValuesForKey(eventType);
        list.add(data);
        if(delayed) temporaryData.add(eventType, data);
        super.notify(eventType, data);*/
    }

    public <EventType extends Enum, Datatype> ArrayList<Datatype> add(Class<? extends Event<EventType, Datatype>> eventClass, EventType eventType) {
        MultiMap<EventType, Datatype> eventMap = (MultiMap<EventType, Datatype>) temporaryData.get(eventClass);
        if(eventMap == null)
            temporaryData.put(eventClass, eventMap = new MultiMap<>());
        System.out.println(eventMap);
        System.out.println(temporaryData.get(eventClass));
        return null;
    }

}
