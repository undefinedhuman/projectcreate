package de.undefinedhuman.projectcreate.engine.event;

import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;

import java.time.Instant;
import java.util.Date;

public class Event {
    @Metadata
    public final String eventType = this.getClass().toString();
    public final Date timestamp = Date.from(Instant.now());
}
