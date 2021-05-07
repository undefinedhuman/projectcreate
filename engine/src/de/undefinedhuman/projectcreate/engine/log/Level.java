package de.undefinedhuman.projectcreate.engine.log;

public enum Level {
    NONE(""),
    INFO("Info"),
    ERROR("Error"),
    DEBUG("Debug"),
    CRASH("Crash");

    private String prefix;

    Level(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
