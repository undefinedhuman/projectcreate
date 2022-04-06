package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.undefinedhuman.projectcreate.engine.event.Event;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
public class MetadataBucket {

    private static final Gson GSON = new GsonBuilder().create();

    private final HashMap<String, MetadataContainer<?>> data = new HashMap<>();
    private final int amountOfEvents;

    private long minTime = Long.MAX_VALUE, maxTime = Long.MIN_VALUE;

    public MetadataBucket(List<Event> events) {
        this.amountOfEvents = events.size();
        events.forEach(this::parseEvent);
    }

    private <E extends Event> void parseEvent(E event) {
        if(event.timestamp < minTime) minTime = event.timestamp;
        if(event.timestamp > maxTime) maxTime = event.timestamp;
        for(MetadataUtils.MetadataFieldWrapper wrapper : MetadataUtils.getMetadataWrappersForEvent(event.getClass())) {
            MetadataContainer metadataContainer = data.computeIfAbsent(wrapper.getKey(), s -> MetadataUtils.createMetadataContainerInstance(wrapper));
            if(metadataContainer == null)
                continue;
            Object value = wrapper.parseValue(event);
            if(value == null) continue;
            metadataContainer.addValue(value);
        }
    }

    public JsonObject toJSON() {
        JsonObject bucket = new JsonObject();
        bucket.add("amountOfEvents", GSON.toJsonTree(amountOfEvents));
        bucket.add("timestamp-min", GSON.toJsonTree(minTime));
        bucket.add("timestamp-max", GSON.toJsonTree(maxTime));
        JsonObject metadata = new JsonObject();
        data.forEach((key, value) -> metadata.add(key, value.toJSON()));
        bucket.add("metadata", metadata);
        return bucket;
    }

}
