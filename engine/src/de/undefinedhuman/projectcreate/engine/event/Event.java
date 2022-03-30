package de.undefinedhuman.projectcreate.engine.event;

import de.undefinedhuman.projectcreate.engine.event.annotations.Metadata;

public class Event {

    @Metadata
    public final long timestamp = System.currentTimeMillis();

}
