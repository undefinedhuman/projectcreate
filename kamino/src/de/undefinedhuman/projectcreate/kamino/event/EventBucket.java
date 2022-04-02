package de.undefinedhuman.projectcreate.kamino.event;

import de.undefinedhuman.projectcreate.engine.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class EventBucket {

    private static final int EVENT_THRESHOLD = 5000;

    private final ArrayList<Event> events = new ArrayList<>();
    private final List<Event> unmodifiableEvents = Collections.unmodifiableList(events);
    private long minTime = Long.MAX_VALUE, maxTime = Long.MIN_VALUE;
    private boolean isFull = false;

    void addEvent(Event event) {
        if(events.contains(event)) return;
        if(event.timestamp > minTime) minTime = event.timestamp;
        if(event.timestamp < maxTime) maxTime = event.timestamp;
        this.events.add(event);
        if(events.size() >= EVENT_THRESHOLD) isFull = true;
    }

    public List<Event> getEvents() {
        return unmodifiableEvents;
    }

    public boolean isFull() {
        return isFull;
    }

}
