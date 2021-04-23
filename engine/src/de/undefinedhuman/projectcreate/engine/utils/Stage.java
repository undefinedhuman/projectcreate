package de.undefinedhuman.projectcreate.engine.utils;

import java.util.Arrays;

public enum Stage {
    INDEV,
    ALPHA,
    BETA,
    RELEASE;

    public static Stage parse(String value) {
        return Arrays
                .stream(Stage.values())
                .filter(stage -> stage.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
