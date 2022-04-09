package de.undefinedhuman.projectcreate.kamino.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.undefinedhuman.projectcreate.engine.event.Event;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventBucket {

    private static final Gson GSON = new GsonBuilder().create();

    private final ArrayList<Event> events = new ArrayList<>();
    private final List<Event> immutableEvents = Collections.unmodifiableList(events);

    public void add(Event event) {
        if(events.contains(event)) return;
        this.events.add(event);
    }

    public List<Event> getEvents() {
        return immutableEvents;
    }

    public int size() {
        return events.size();
    }

    public String toJSON() {
        return GSON.toJson(events);
    }

    public byte[] serialize() {
        return toJSON().getBytes(StandardCharsets.UTF_8);
    }

}
