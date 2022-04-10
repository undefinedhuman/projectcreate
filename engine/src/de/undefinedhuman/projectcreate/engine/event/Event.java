package de.undefinedhuman.projectcreate.engine.event;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.Date;

public class Event {

    @Metadata
    public final String eventType = this.getClass().toString();
    public final long timestamp = new Date().getTime();
}
