package de.undefinedhuman.projectcreate.engine.ecs.annotations;

import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RequiredComponents {
    Class<? extends ComponentBlueprint>[] value() default {};
}
