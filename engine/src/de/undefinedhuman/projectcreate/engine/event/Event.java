package de.undefinedhuman.projectcreate.engine.event;

import com.playprojectcreate.kaminoapi.annotations.Metadata;
import com.playprojectcreate.kaminoapi.query.QueryExcluded;

import java.util.Date;

public class Event {

    @Metadata
    @QueryExcluded
    public final String eventType = this.getClass().getCanonicalName();

    public final long timestamp = new Date().getTime();
}
