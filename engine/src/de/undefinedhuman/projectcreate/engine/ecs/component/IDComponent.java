package de.undefinedhuman.projectcreate.engine.ecs.component;

import com.badlogic.ashley.core.Component;

public class IDComponent implements Component {
    public long worldID = -1;
    public int blueprintID = -1;

    public IDComponent() {}

    public IDComponent(long worldID) {
        this.worldID = worldID;
    }

    public IDComponent(int blueprintID) {
        this.blueprintID = blueprintID;
    }

    public IDComponent(long worldID, int blueprintID) {
        this.worldID = worldID;
        this.blueprintID = blueprintID;
    }

}
