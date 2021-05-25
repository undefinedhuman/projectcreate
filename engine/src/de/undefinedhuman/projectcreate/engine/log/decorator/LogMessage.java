package de.undefinedhuman.projectcreate.engine.log.decorator;

import java.util.function.Function;

public class LogMessage implements Function<String, String> {
    @Override
    public String apply(String s) {
        return s;
    }
}
