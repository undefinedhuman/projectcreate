package de.undefinedhuman.projectcreate.kamino.event.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class MetadataContainer<T> {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER = Map.of(
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            char.class, Character.class,
            double.class, Double.class,
            float.class, Float.class,
            int.class, Integer.class,
            long.class, Long.class,
            short.class, Short.class
    );
    protected static final Gson GSON = new GsonBuilder().create();

    private final Class<?> type;

    public MetadataContainer(Class<T> type) {
        this.type = type;
    }

    public boolean verifyField(Field field) {
        Class<?> fieldType = field.getType();
        if(fieldType.isPrimitive()) fieldType = PRIMITIVE_WRAPPER.get(fieldType);
        return fieldType == type;
    }

    public abstract void addValue(T t);
    public abstract JsonElement toJSON();

}
