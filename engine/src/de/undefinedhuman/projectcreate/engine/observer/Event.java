package de.undefinedhuman.projectcreate.engine.observer;

public class Event<T, E> {

    private Class<T> eventType;
    private Class<E> dataType;

    protected Event(Class<T> eventType, Class<E> dataType) {
        this.eventType = eventType;
        this.dataType = dataType;
    }

    public Class<T> getEventType() {
        return eventType;
    }

    public Class<E> getDataType() {
        return dataType;
    }

}