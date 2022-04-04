package de.undefinedhuman.projectcreate.kamino.event;

import com.google.gson.Gson;
import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MetadataBucket {

    private final HashMap<String, HashSet<Object>> data = new HashMap<>();

    public MetadataBucket(List<Event> events) {
        events.forEach(this::parseEvent);
    }

    private <E extends Event> void parseEvent(E event) {
        Field[] fields = event.getClass().getFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Metadata.class))
                .forEach(field -> {
                    Metadata metadata = field.getAnnotation(Metadata.class);
                    String key = metadata.name();
                    if(metadata.name().equalsIgnoreCase("NOT_DEFINED")) key = field.getName();
                    Object value;
                    try {
                        value = field.get(event);
                    } catch (IllegalAccessException ex) {
                        Log.error("Error while parsing value of event field " + key + "!", ex);
                        return;
                    }
                    HashSet<Object> set = data.computeIfAbsent(key, k -> new HashSet<>());
                    set.add(value);
                });
    }

    public void print() {
        Log.info(new Gson().toJson(data));
    }

}
