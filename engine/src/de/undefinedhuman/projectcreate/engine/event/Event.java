package de.undefinedhuman.projectcreate.engine.event;

public class Event {
    public final String eventType = this.getClass().getName();
    public final long timestamp = System.currentTimeMillis();
}
