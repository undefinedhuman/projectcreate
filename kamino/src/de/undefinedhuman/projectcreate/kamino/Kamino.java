package de.undefinedhuman.projectcreate.kamino;

import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.kamino.query.QueryUtils;

public class Kamino {

    public static void registerEvents(Class<? extends Event>... events) {
        QueryUtils.addEventTypes(events);
    }

}
