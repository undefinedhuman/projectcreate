package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.event.Event;

public class FamilyEvent extends Event {

    public final Type type;
    public final int familyIndex;
    public final Entity[] entities;

    public FamilyEvent(Type type, int familyIndex, Entity... entities) {
        this.type = type;
        this.familyIndex = familyIndex;
        this.entities = entities;
    }

    public FamilyEvent(Type type, Family family, Entity... entities) {
        this(type, family.getIndex(), entities);
    }

    public enum Type {
        ADD,
        REMOVE,
        UPDATE
    }

}
