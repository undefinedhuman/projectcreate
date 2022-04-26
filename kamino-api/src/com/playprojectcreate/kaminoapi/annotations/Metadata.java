package com.playprojectcreate.kaminoapi.annotations;

import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Metadata {
    String databaseName() default Utils.METADATA_DEFAULT_NAME;
    Class<? extends MetadataContainer> containerType() default MetadataContainer.class;
    String index() default "";

    class Utils {

        public static final String METADATA_DEFAULT_NAME = "NOT_DEFINED";

        public static String getNameOfMetadataField(Field field) {
            if(!field.isAnnotationPresent(Metadata.class))
                return null;
            Metadata metadata = field.getAnnotation(Metadata.class);
            return metadata.databaseName().equalsIgnoreCase(METADATA_DEFAULT_NAME) ? (field.getName() + "s") : metadata.databaseName().replaceAll("[^A-Z0-9a-z]", "");
        }

    }

}

