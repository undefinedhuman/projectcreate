package com.playprojectcreate.kaminoapi.annotations;

import com.playprojectcreate.kaminoapi.metadata.ContainerUtils;
import com.playprojectcreate.kaminoapi.metadata.MetadataContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Metadata {
    String name() default ContainerUtils.METADATA_DEFAULT_NAME;
    Class<? extends MetadataContainer> containerType() default MetadataContainer.class;
}