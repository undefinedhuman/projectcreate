package de.undefinedhuman.projectcreate.kamino.event.events;

import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;

public class TestEvent extends Event {

    @Metadata
    public final int id;

    public TestEvent(int id) {
        this.id = id;
    }

}
