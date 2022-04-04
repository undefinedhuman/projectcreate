package de.undefinedhuman.projectcreate.kamino.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.undefinedhuman.projectcreate.engine.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventBucket {

    private static final Gson GSON = new GsonBuilder().create();
    private static final int EVENT_THRESHOLD = 5000;

    private final ArrayList<Event> events = new ArrayList<>();
    private final List<Event> immutableEvents = Collections.unmodifiableList(events);
    private long minTime = Long.MAX_VALUE, maxTime = Long.MIN_VALUE;
    private boolean isFull = false;

    public void addEvent(Event event) {
        if(events.contains(event)) return;
        if(event.timestamp > minTime) minTime = event.timestamp;
        if(event.timestamp < maxTime) maxTime = event.timestamp;
        this.events.add(event);
        if(events.size() >= EVENT_THRESHOLD) isFull = true;
    }

    public List<Event> getEvents() {
        return immutableEvents;
    }

    public boolean isFull() {
        return isFull;
    }

    public String toString() {
        return GSON.toJson(events);
    }

}
