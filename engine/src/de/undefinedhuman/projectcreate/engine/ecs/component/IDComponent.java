package de.undefinedhuman.projectcreate.engine.ecs.component;

import com.badlogic.ashley.core.Component;

public class IDComponent implements Component {
    public static final int UNDEFINED = -1;

    private long worldID = UNDEFINED;
    private int blueprintID = UNDEFINED;

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

    public long getWorldID() {
        return worldID;
    }

    public int getBlueprintID() {
        return blueprintID;
    }
}
