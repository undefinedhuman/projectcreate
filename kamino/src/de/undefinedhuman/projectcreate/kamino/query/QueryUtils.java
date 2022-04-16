package de.undefinedhuman.projectcreate.kamino.query;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.playprojectcreate.kaminoapi.metadata.ContainerUtils;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataField;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class QueryUtils {

    private static final MultiMap<Class<? extends Event>, QueryField> EVENT_QUERY_TYPES = new MultiMap<>();
    private static final JsonArray CACHED_QUERY_TYPES = new JsonArray();

    private static final Set<Class<?>> IMPLEMENTED_CLASSES = Set.of(
            Boolean.class,
            String.class,
            Character.class,
            Integer.class,
            Byte.class,
            Short.class,
            Long.class,
            Float.class,
            Double.class,
            Vector2.class
    );

    public static JsonArray getQueryTypesJson() {
        return CACHED_QUERY_TYPES;
    }

    @SafeVarargs
    public static void addEventTypes(Class<? extends Event>... eventTypes) {
        for(Class<? extends Event> eventType : eventTypes) {
            if(EVENT_QUERY_TYPES.containsKey(eventType)) continue;
            EVENT_QUERY_TYPES.set(eventType, parseEventQueryTypes(eventType));
            CACHED_QUERY_TYPES.add(getJsonOfEventType(eventType));
        }
    }

    private static JsonObject getJsonOfEventType(Class<? extends Event> eventType) {
        JsonObject eventQueryType = new JsonObject();
        if(!EVENT_QUERY_TYPES.containsKey(eventType))
            return eventQueryType;
        JsonObject fieldTypes = new JsonObject();
        for(QueryField fieldType : EVENT_QUERY_TYPES.getDataForKey(eventType))
            fieldType.toJSON(fieldTypes);
        eventQueryType.addProperty("eventType", eventType.getCanonicalName());
        eventQueryType.add("fields", fieldTypes);
        return eventQueryType;
    }

    private static ArrayList<QueryField> parseEventQueryTypes(Class<? extends Event> eventType) {
        ArrayList<QueryField> wrappers = new ArrayList<>();
        ImmutableArray<MetadataField> metadataFieldsOfEvent = MetadataUtils.getMetadataFieldsForEvent(eventType);
        for(MetadataField metadataField : metadataFieldsOfEvent) {
            if(metadataField.isExcludedFromQueries()) continue;
            wrappers.add(parseClass(metadataField.getKey(), metadataField.getQueryType()));
        }
        return wrappers;
    }

    private static QueryField parseClass(String key, Class<?> type) {
        type = ContainerUtils.wrapPrimitive(type);
        if(IMPLEMENTED_CLASSES.contains(type)) return new QueryField(key, type);
        else return new QueryField(key, parseFields(type.getDeclaredFields()));
    }

    private static QueryField[] parseFields(Field[] fields) {
        return Arrays.stream(fields)
                .map(field -> parseClass(field.getName(), field.getType()))
                .toArray(QueryField[]::new);
    }

    static class QueryField {
        private final String key;
        private final Class<?> type;
        private final QueryField[] children;

        public QueryField(String key, Class<?> type) {
            this.key = key;
            this.type = type;
            this.children = null;
        }

        public QueryField(String key, QueryField[] children) {
            this.key = key;
            this.type = null;
            this.children = children;
        }

        public void toJSON(JsonObject parent) {
            if(type != null) parent.addProperty(key, type.getSimpleName());
            if(children != null && children.length != 0) {
                JsonObject childTypeParent = new JsonObject();
                for(QueryField childType : children)
                    childType.toJSON(childTypeParent);
                parent.add(key, childTypeParent);
            }
        }

    }
}
