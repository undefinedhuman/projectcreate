package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.observer.Event;
import de.undefinedhuman.projectcreate.engine.observer.EventManager;
import de.undefinedhuman.projectcreate.engine.observer.Observer;

public class FamilyEvent extends Event<Integer, Entity[]> {

    // Multiple of 10
    private static final int MAX_TYPES = 10;

    public enum Type {
        ADD,
        REMOVE,
        UPDATE
    }

    protected FamilyEvent() {
        super(Integer.class, Entity[].class);
    }

    public static void subscribe(EventManager eventManager, Family family, Type type, Observer<Entity[]> observer) {
        eventManager.subscribe(FamilyEvent.class, createEventID(family, type), observer);
    }

    public static void unsubscribe(EventManager eventManager, Family family, Type type, Observer<Entity[]> observer) {
        eventManager.unsubscribe(FamilyEvent.class, createEventID(family, type), observer);
    }

    static int createEventID(Family family, Type eventType) {
        return family.getIndex() * MAX_TYPES + min(eventType.ordinal());
    }

    private static int min(int type) {
        if(type <= MAX_TYPES-2)
            return type;
        Log.warn("Type of family event should not exceed " + (MAX_TYPES-2) + "!");
        return MAX_TYPES-1;
    }

}
