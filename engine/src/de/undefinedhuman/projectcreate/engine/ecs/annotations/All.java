package de.undefinedhuman.projectcreate.engine.ecs.annotations;

import de.undefinedhuman.projectcreate.engine.ecs.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface All {
    Class<? extends Component>[] value() default {};
}
