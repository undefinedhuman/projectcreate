package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.badlogic.gdx.utils.Array;
import com.playprojectcreate.kaminoapi.metadata.ContainerUtils;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import com.playprojectcreate.kaminoapi.metadata.container.BasicMetadataContainer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetadataUtils {

    private static final Map<Class<? extends Event>, ImmutableArray<MetadataFieldWrapper>> METADATA_FIELD_WRAPPERS = Collections.synchronizedMap(new HashMap<>());
    private static final HashMap<String, Tuple<Class<? extends Event>, Class<?>>> FIELD_TYPES = new HashMap<>();

    public static synchronized ImmutableArray<MetadataFieldWrapper> getMetadataWrappersForEvent(Class<? extends Event> eventType) {
        return METADATA_FIELD_WRAPPERS.computeIfAbsent(eventType, aClass -> {
            Array<MetadataFieldWrapper> fieldData = new Array<>();
            Arrays.stream(eventType.getFields())
                    .peek(field -> Log.info(field.getName(), Arrays.toString(field.getAnnotations())))
                    .filter(field -> field.isAnnotationPresent(Metadata.class))
                    .filter(field -> {
                        Metadata metadata = field.getAnnotation(Metadata.class);
                        if(!registerFieldType(eventType, parseMetadataFieldKey(field, metadata), field)) return false;
                        MetadataContainer<?> metadataContainer = createMetadataContainerInstance(field, metadata);
                        return metadataContainer != null && metadataContainer.verifyField(field);
                    }).map(MetadataFieldWrapper::new)
                    .forEach(fieldData::add);
            return new ImmutableArray<>(fieldData);
        });
    }

    private static boolean registerFieldType(Class<? extends Event> eventClass, String key, Field field) {
        Tuple<Class<? extends Event>, Class<?>> currentFieldTypeEntry = FIELD_TYPES.get(key);
        if(currentFieldTypeEntry != null && currentFieldTypeEntry.getU() != field.getType()) {
            Log.warn("Error while registering event metadata field! Type mismatch with already registered field (same field key)! Metadata wont be collected for this field! Should probably be reported to the maintainer! Proceed with caution!" +
                    "\nKey: " + key +
                    "\nEvent type of registered field: " + currentFieldTypeEntry.getT() +
                    "\nRegistered field type: " + currentFieldTypeEntry.getU() +
                    "\nEvent type of new field: " + eventClass +
                    "\nField type: " + field.getType());
            return false;
        }
        FIELD_TYPES.put(key, new Tuple<>(eventClass, field.getType()));
        return true;
    }

    public static MetadataContainer<?> createMetadataContainerInstance(MetadataFieldWrapper wrapper) {
        return createMetadataContainerInstance(wrapper.field, wrapper.metadata);
    }

    private static MetadataContainer<?> createMetadataContainerInstance(Field field, Metadata metadata) {
        MetadataContainer<?> metadataContainer;
        try {
            if(metadata.containerType() == MetadataContainer.class || metadata.containerType() == null)
                metadataContainer = new BasicMetadataContainer<>(ContainerUtils.wrapPrimitive(field.getType()));
            else metadataContainer = metadata.containerType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.warn("Error while creating metadata container for " + parseMetadataFieldKey(field, metadata) + "! Metadata wont be collected for this field! Should probably be reported to the maintainer! Proceed with caution!");
            return null;
        }
        if(!metadataContainer.verifyField(field)) {
            Log.warn("Metadata container doesnt match type of field! Metadata wont be collected for this field! Should probably be reported to the maintainer! Proceed with caution!" +
                    "\n Field: " + parseMetadataFieldKey(field, metadata) +
                    "\n Container: " + metadataContainer.getClass() +
                    "\n Container Type: " + metadataContainer.getType());
            return null;
        }
        return metadataContainer;
    }

    private static String parseMetadataFieldKey(Field field, Metadata metadata) {
        return metadata.name().equalsIgnoreCase(ContainerUtils.METADATA_DEFAULT_NAME) ? field.getName() : metadata.name();
    }

    static class MetadataFieldWrapper {

        private final Metadata metadata;
        private final String key;
        private final Field field;

        private MetadataFieldWrapper(Field field) {
            this.metadata = field.getAnnotation(Metadata.class);
            this.key = parseMetadataFieldKey(field, metadata);
            this.field = field;
        }

        public Field getField() {
            return field;
        }

        public String getKey() {
            return key;
        }

        Object parseValue(Event event) {
            try {
                return field.get(event);
            } catch (IllegalAccessException ex) {
                Log.error("Error while parsing value of event field " + key + "!", ex);
                return null;
            }
        }

    }

}
