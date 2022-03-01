package de.undefinedhuman.projectcreate.server.utils.commands.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    String name();
    String[] aliases() default "";
    String description() default "";
    String usage() default "";
}
