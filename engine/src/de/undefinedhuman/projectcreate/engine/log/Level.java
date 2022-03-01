package de.undefinedhuman.projectcreate.engine.log;

public enum Level {
    NONE(""),
    ERROR("Error"),
    WARN("Warn"),
    INFO("Info"),
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
