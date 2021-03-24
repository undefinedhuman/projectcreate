package de.undefinedhuman.projectcreate.engine.language;

public class Replacement {

    private String name, value;

    public Replacement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
