package de.undefinedhuman.projectcreate.engine.log;

public enum Level {
    NONE(""),
    INFO("Info"),
    ERROR("Error"),
    WARN("Warn"),
    CRASH("Crash"),
    DEBUG("Debug");

    private final String prefix;

    Level(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        if(prefix.equals(""))
            return "";
        return "[" + prefix + "] ";
    }
}
