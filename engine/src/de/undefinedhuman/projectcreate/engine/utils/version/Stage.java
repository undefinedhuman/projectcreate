package de.undefinedhuman.projectcreate.engine.utils.version;

import java.util.Arrays;

public enum Stage {
    SNAPSHOT("snapshot-"),
    INDEV("indev-"),
    ALPHA("alpha-"),
    BETA("beta-"),
    RELEASE("");

    private String prefix;

    Stage(String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }

    public static Stage parse(String versionString) {
        return Arrays.stream(Stage.values())
                .filter(stage -> !stage.prefix.equals(""))
                .filter(stage -> versionString.startsWith(stage.prefix))
                .findFirst()
                .orElse(Stage.RELEASE);
    }
}
