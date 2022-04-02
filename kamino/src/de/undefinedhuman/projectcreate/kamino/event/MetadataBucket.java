package de.undefinedhuman.projectcreate.kamino.event;

import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.event.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MetadataBucket {

    private final HashSet<Class<? extends Event>> eventClasses = new HashSet<>();
    private final HashMap<String, HashSet<Object>> data = new HashMap<>();

    public <E extends Event> void parseEvent(E event) {
        eventClasses.add(event.getClass());
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

    public void exportJSON() {
    }

    public void print() {
        Log.info("Events: " + eventClasses);
        Log.info("Data: ");
        for (Map.Entry<String, HashSet<Object>> stringHashSetEntry : data.entrySet()) {
            Log.info(stringHashSetEntry.getKey() + ": " + stringHashSetEntry.getValue().toString());
        }
    }

}
