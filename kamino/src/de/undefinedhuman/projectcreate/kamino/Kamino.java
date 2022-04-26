package de.undefinedhuman.projectcreate.kamino;

import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.kamino.database.Database;
import de.undefinedhuman.projectcreate.kamino.query.QueryUtils;

public class Kamino {

    static QueryUtils QUERY_UTILS;
    static Database DATABASE;

    public static void registerEvents(Class<? extends Event>... events) {
        QUERY_UTILS.addEventTypes(events);
    }

    public static void parseRequest() {

    }

}
