package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.All;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.Exclude;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.One;
import de.undefinedhuman.projectcreate.engine.ecs.annotations.Optional;

public class Metadata {

    static Family parseSystemMetadata(Class<? extends System> systemClass) {
        return new Family.FamilyBuilder()
                .with(Family::all, getAnnotation(systemClass, All.class))
                .with(Family::one, getAnnotation(systemClass, One.class))
                .with(Family::exclude, getAnnotation(systemClass, Exclude.class))
                .with(Family::optional, getAnnotation(systemClass, Optional.class))
                .build();
    }

    static <T extends java.lang.annotation.Annotation> T getAnnotation(Class<?> objectClass, Class<T> annotationClass) {
        final Annotation annotation = ClassReflection.getAnnotation(objectClass, annotationClass);
        if(annotation == null)
            return null;
        return annotation.getAnnotation(annotationClass);
    }

}
