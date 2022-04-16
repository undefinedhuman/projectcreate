package de.undefinedhuman.projectcreate.kamino.event.events;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

@KaminoEvent
public class TestEvent extends Event {

    @Metadata
    public final int id;

    public TestEvent(int id) {
        this.id = id;
    }

}
