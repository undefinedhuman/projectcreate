package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.google.gson.JsonObject;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;
import de.undefinedhuman.projectcreate.engine.event.Event;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
public class MetadataBucket {

    private final HashMap<String, MetadataContainer<?>> data = new HashMap<>();
    private final int amountOfEvents;
    private final int decompressedEventSize;

    private final String eventDataID;
    private long minTime = Long.MAX_VALUE, maxTime = Long.MIN_VALUE;

    public MetadataBucket(String eventDataID, int decompressedEventSize, List<Event> events) {
        this.eventDataID = eventDataID;
        this.decompressedEventSize = decompressedEventSize;
        this.amountOfEvents = events.size();
        events.forEach(this::parseEvent);
    }

    private <E extends Event> void parseEvent(E event) {
        if(event.timestamp < minTime) minTime = event.timestamp;
        if(event.timestamp > maxTime) maxTime = event.timestamp;
        for(MetadataField metadataField : MetadataUtils.getMetadataFieldsForEvent(event.getClass())) {
            MetadataContainer metadataContainer = data.computeIfAbsent(metadataField.getKey(), s -> metadataField.createMetadataContainer());
            if(metadataContainer == null)
                continue;
            Object value = metadataField.parseValue(event);
            if(value != null) metadataContainer.addValue(value);
        }
    }

    public String toJSON() {
        JsonObject bucket = new JsonObject();
        bucket.addProperty("amountOfEvents", amountOfEvents);
        bucket.addProperty("eventDataID", eventDataID);
        bucket.addProperty("decompressedSize", decompressedEventSize);
        bucket.addProperty("timestamp-min", minTime);
        bucket.addProperty("timestamp-max", maxTime);
        JsonObject metadata = new JsonObject();
        for(String key : data.keySet())
            metadata.add(key, data.get(key).toJSON());
        bucket.add("metadata", metadata);
        return bucket.toString();
    }

}
