package com.playprojectcreate.kaminoapi.metadata;

import java.util.Map;

public class ContainerUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER = Map.of(
            int.class, Integer.class,
            boolean.class, Boolean.class,
            long.class, Long.class,
            double.class, Double.class,
            char.class, Character.class,
            float.class, Float.class,
            byte.class, Byte.class,
            short.class, Short.class
    );

    public static Class<?> wrapPrimitive(Class<?> primitiveClass) {
        if(!primitiveClass.isPrimitive()) return primitiveClass;
        return PRIMITIVE_WRAPPER.get(primitiveClass);
    }

}
