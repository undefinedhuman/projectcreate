package de.undefinedhuman.projectcreate.kamino.annotations;

import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataContainer;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Metadata {
    String name() default MetadataUtils.METADATA_DEFAULT_NAME;
    Class<? extends MetadataContainer> containerType() default MetadataContainer.class;
}