package de.undefinedhuman.projectcreate.engine.event;

import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;

import java.util.Date;

public class Event {
    @Metadata
    public final String eventType = this.getClass().toString();
    public final long timestamp = new Date().getTime();
}
