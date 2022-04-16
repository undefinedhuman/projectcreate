package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import com.playprojectcreate.kaminoapi.metadata.ContainerUtils;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;
import com.playprojectcreate.kaminoapi.metadata.container.primitive.BasicMetadataContainer;
import com.playprojectcreate.kaminoapi.query.QueryExcluded;
import com.playprojectcreate.kaminoapi.query.QueryParameter;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class MetadataField {

    private final Class<?> queryType;
    private final Class<?> fieldType;
    private final Class<? extends Event> eventType;
    private final Metadata metadata;
    private final boolean excludedFromQueries;
    private final String key;
    private final Field field;

    private MetadataField(Class<? extends Event> eventType, Field field) {
        this.eventType = eventType;
        this.metadata = field.getAnnotation(Metadata.class);
        this.key = Metadata.Utils.getNameOfMetadataField(field);
        this.field = field;
        this.fieldType = ContainerUtils.wrapPrimitive(field.getType());
        this.excludedFromQueries = field.isAnnotationPresent(QueryExcluded.class);
        this.queryType = parseQueryType(fieldType, metadata);
    }

    public static MetadataField create(Class<? extends Event> eventType, Field field) {
        if(field == null || !field.isAnnotationPresent(Metadata.class))
            return null;
        MetadataField metadataField = new MetadataField(eventType, field);
        MetadataContainer<?> metadataContainer = metadataField.createMetadataContainer();
        if(metadataContainer == null || !metadataContainer.verifyField(field)) return null;
        return metadataField;
    }

    public String getKey() {
        return key;
    }

    public String getEventTypeName() {
        return eventType.getCanonicalName();
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Class<?> getQueryType() {
        return queryType;
    }

    public boolean isExcludedFromQueries() {
        return excludedFromQueries;
    }

    public Object parseValue(Event event) {
        try {
            return field.get(event);
        } catch (IllegalAccessException ex) {
            Log.error("Error while parsing value of event field " + key + "!", ex);
            return null;
        }
    }

    public MetadataContainer<?> createMetadataContainer() {
        MetadataContainer<?> metadataContainer;
        try {
            if(metadata.containerType() == MetadataContainer.class || metadata.containerType() == BasicMetadataContainer.class || metadata.containerType() == null)
                metadataContainer = new BasicMetadataContainer<>(fieldType);
            else metadataContainer = metadata.containerType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.warn("Error while creating metadata container for " + key + "! Metadata wont be collected for this field! Should probably be reported to the maintainer! Proceed with caution!");
            return null;
        }
        return metadataContainer;
    }

    private static Class<?> parseQueryType(Class<?> defaultType, Metadata metadata) {
        if(defaultType == null || metadata == null) return null;
        QueryParameter queryParameter = metadata.containerType().getAnnotation(QueryParameter.class);
        return queryParameter != null ? ContainerUtils.wrapPrimitive(queryParameter.type()) : defaultType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetadataField that = (MetadataField) o;
        return excludedFromQueries == that.excludedFromQueries && Objects.equals(queryType, that.queryType) && Objects.equals(fieldType, that.fieldType) && Objects.equals(eventType, that.eventType) && Objects.equals(metadata, that.metadata) && Objects.equals(key, that.key) && Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryType, fieldType, eventType, metadata, excludedFromQueries, key, field);
    }
}
