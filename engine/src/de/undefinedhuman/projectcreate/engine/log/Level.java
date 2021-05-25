package de.undefinedhuman.projectcreate.engine.log;

public enum Level {
    NONE(""),
    INFO("Info"),
    ERROR("Error"),
    DEBUG("Debug"),
    CRASH("Crash");

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
