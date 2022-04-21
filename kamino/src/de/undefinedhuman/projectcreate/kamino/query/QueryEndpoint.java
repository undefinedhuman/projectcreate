package de.undefinedhuman.projectcreate.kamino.query;

import com.google.gson.*;
import com.playprojectcreate.kaminoapi.query.Query;
import com.playprojectcreate.kaminoapi.query.QueryParameterWrapper;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;
import de.undefinedhuman.projectcreate.kamino.database.Database;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataField;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataUtils;
import de.undefinedhuman.projectcreate.kamino.utils.Decompressor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public class QueryEndpoint {

    private static final String ALPHA_NUMERIC_REGEX = "[^A-Za-z]";

    // TODO CACHING

    public static String parseRequest(Database database, String request, Decompressor decompressor, boolean includeAllEvents) {
        try {
            JsonArray array = new Gson().fromJson(request, JsonArray.class);
            return parseRequest(database, array, decompressor, includeAllEvents).toString();
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }

    public static JsonArray parseRequest(Database database, JsonArray request, Decompressor decompressor, boolean includeAllEvents) {
        StringBuilder query = new StringBuilder();
        HashMap<String, Object> parameters = new HashMap<>();
        MultiMap<String, JsonObject> requestEventDataFields = new MultiMap<>();
        for(int i = 0; i < request.size(); i++) {
            if(!(request.get(i) instanceof JsonObject jsonObject)) continue;
            Class<? extends Event> eventType;
            try {
                if(!(jsonObject.get("eventTypes") instanceof JsonPrimitive eventTypeObject)) continue;
                Class<?> parsedClass = Class.forName(eventTypeObject.getAsString());
                if(!(Event.class.isAssignableFrom(parsedClass))) {
                    Log.warn("Class does not inherit event class");
                    continue;
                }
                eventType = (Class<? extends Event>) parsedClass;
            } catch (Exception ex) {
                Log.info("EventType is not class which inherits Event");
                continue;
            }
            if(!(jsonObject.get("fields") instanceof JsonObject fields)) continue;
            ImmutableArray<MetadataField> metadataFields = MetadataUtils.getMetadataFieldsForEvent(eventType);
            requestEventDataFields.add(eventType.getCanonicalName(), fields);
            query.append(i != 0 ? " OR " : "").append("(ARRAY_CONTAINS(").append(database.getTableName()).append(".eventTypes, ").append("\"").append(eventType.getCanonicalName()).append("\")");
            metadataFields.forEach(metadataField -> {
                if(!fields.has(metadataField.getKey())) return;
                // TODO CATCH JSON SYNTAX EXCEPTION
                Object parsedRequestParameter = new Gson().fromJson(fields.get(metadataField.getKey()), metadataField.getQueryType());
                if(parsedRequestParameter == null) return;
                QueryParameterWrapper<?>[] queryParameters = parseQueryParameter(metadataField.createMetadataContainer().getQuery(), metadataField.getKey(), parsedRequestParameter);
                String[] parameterKeys = Arrays.stream(queryParameters)
                        .map(QueryParameterWrapper::key)
                        .map(queryParameterKey -> (metadataField.getEventTypeName() + queryParameterKey).replaceAll(ALPHA_NUMERIC_REGEX, ""))
                        .toArray(String[]::new);
                for(int j = 0; j < queryParameters.length; j++) {
                    Object parameter = queryParameters[j].value();
                    if(parameter instanceof Float value)
                        parameter = BigDecimal.valueOf(value);
                    if(parameter instanceof Short value)
                        parameter = Integer.valueOf(value);
                    if(parameter instanceof Byte value)
                        parameter = Integer.valueOf(value);
                    if(parameter instanceof Character value)
                        parameter = String.valueOf(value);
                    parameters.put(parameterKeys[j], parameter);
                }
                query.append(" AND ").append(metadataField.createMetadataContainer().getQuery().createQuery(database.getTableName() + "." + metadataField.getKey(), parameterKeys));
            });
            query.append(")");
        }
        JsonArray output = new JsonArray();
        for(Tuple<String, Integer> eventBucketID : database.searchMetadata(query.toString(), parameters)) {
            byte[] compressedData = database.searchEvent(eventBucketID.getT());
            byte[] uncompressedData = decompressor.decompress(compressedData, eventBucketID.getU());
            String data = new String(uncompressedData);
            JsonArray array = new Gson().fromJson(data, JsonArray.class);
            if(includeAllEvents) {
                output.addAll(array);
                continue;
            }
            array.forEach(eventDataElement -> {
                if(!(eventDataElement instanceof JsonObject eventData)) return;
                String eventKey = eventData.get("eventType").getAsString();
                if(!requestEventDataFields.containsKey(eventKey)) return;
                Class<? extends Event> eventType;
                try {
                    Class<?> parsedClass = Class.forName(eventKey);
                    if(!Event.class.isAssignableFrom(parsedClass)) return;
                    eventType = (Class<? extends Event>) parsedClass;
                } catch (ClassNotFoundException e) {
                    Log.error("Can not find event class!");
                    throw new RuntimeException(e);
                }
                Event event = new Gson().fromJson(eventDataElement, eventType);

                boolean eventMatches = false;
                for(JsonObject fields : requestEventDataFields.getDataForKey(eventKey)) {
                    boolean metaFieldsMatch = true;
                    for(MetadataField metadataField : MetadataUtils.getMetadataFieldsForEvent(eventType)) {
                        Object eventValue = metadataField.parseValue(event);
                        Object parsedRequestParameter = new Gson().fromJson(fields.get(metadataField.getKey()), metadataField.getQueryType());
                        if(eventValue == null || parsedRequestParameter == null) continue;
                        if(!metadataField.createMetadataContainer().getQuery().doesEventMatch(parsedRequestParameter, eventValue))
                            metaFieldsMatch = false;
                    }
                    if(metaFieldsMatch) eventMatches = true;
                }
                if(eventMatches) output.add(eventDataElement);
            });
        }
        return output;
    }

    private static <T, E> QueryParameterWrapper<E>[] parseQueryParameter(Query<T, E> query, String key, T value) {
        return query.parseQueryParametersFromRequest(key, value);
    }

}
