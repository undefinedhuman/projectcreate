package de.undefinedhuman.projectcreate.core.ecs.name;

import com.badlogic.ashley.core.Component;

public class NameComponent implements Component {

    private String name;

    public NameComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
