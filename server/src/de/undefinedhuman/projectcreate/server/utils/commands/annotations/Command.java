package de.undefinedhuman.projectcreate.server.utils.commands.annotations;

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

    // TODO MAYBE ADD AN ARRAY OF PARAMS AND CREATE USAGE BASED UPON THOSE PARAMS
    // IS PARAMS ARRAY POSSIBLE? OR SHOULD LOGIC BE HANDLED BY PLUGIN/THIRD PARTY DEVELOPER
    // WHAT IF AN OPTIONAL SECOND PARAMETER DEPENDS UPON A FIRST OPTIONAL PARAMETER?
    // MAYBE ADD DEPENDS ON PARAMETER NAME?
    // ERROR MESSAGE FOR DEPENDS ON?
    // MAYBE ALLOW BOTH - SIMPLE VERSION (IF PARAMETERS IN ANNOTATION ARE PRESENT) - COMPLEX VERSION (IF PARAMETERS IN ANNOTATION ARE NULL JUST PARSE PARAMETERS AS STRING ARRAY AND GIVE STRING ARRAY)
    // HEAL COMMAND:
    // COMMAND /heal
    // ALIAS /h
    // PARAMS:
    // PLAYER_NAME:
    // - TYPE: String
    // - OPTIONAL: true
    // AMOUNT:
    // - TYPE: Integer
    // - OPTIONAL: true

    // USAGE: /heal - Fully heals executor
    // USAGE: /heal AMOUNT - Set health of executor to AMOUNT
    // USAGE: /heal PLAYER_NAME - Fully heal player PLAYER_NAME
    // USAGE: /heal PLAYER_NAME AMOUNT - Set health of player PLAYER_NAME
}
