package com.playprojectcreate.kaminoapi.metadata;

import java.util.Map;

public class ContainerUtils {

    public static final String METADATA_DEFAULT_NAME = "NOT_DEFINED";

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

    public static Class<?> wrapPrimitive(Class<?> primitiveClass) {
        if(!primitiveClass.isPrimitive()) return primitiveClass;
        return PRIMITIVE_WRAPPER.get(primitiveClass);
    }

}
