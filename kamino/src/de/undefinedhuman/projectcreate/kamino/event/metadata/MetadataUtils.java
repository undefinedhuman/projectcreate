package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.badlogic.gdx.utils.Array;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;
import de.undefinedhuman.projectcreate.engine.utils.ds.Tuple;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetadataUtils {

    private static final Map<Class<? extends Event>, ImmutableArray<MetadataField>> METADATA_FIELD_WRAPPERS = Collections.synchronizedMap(new HashMap<>());
    private static final HashMap<String, Tuple<Class<? extends Event>, Class<?>>> FIELD_TYPES = new HashMap<>();

    public static synchronized ImmutableArray<MetadataField> getMetadataFieldsForEvent(Class<? extends Event> eventType) {
        return METADATA_FIELD_WRAPPERS.computeIfAbsent(eventType, MetadataUtils::parseMetadataFieldsForEvent);
    }

    private static ImmutableArray<MetadataField> parseMetadataFieldsForEvent(Class<? extends Event> eventType) {
        Array<MetadataField> metadataFields = new Array<>();
        for(Field field : eventType.getFields()) {
            Metadata metadata = field.getAnnotation(Metadata.class);
            if(metadata == null || !registerFieldTypeForKey(eventType, field)) continue;
            MetadataField metadataField = MetadataField.create(eventType, field);
            if(metadataField == null) {
                Log.warn("Error while parsing metadata configuration for field! Metadata wont be collected for this field! Should probably be reported to the maintainer! Proceed with caution!" +
                        "\n Field: " + field.getName());
                continue;
            }
            metadataFields.add(metadataField);
        }
        return new ImmutableArray<>(metadataFields);
    }

    private static boolean registerFieldTypeForKey(Class<? extends Event> eventClass, Field field) {
        String key = Metadata.Utils.getNameOfMetadataField(field);
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

}
