package de.undefinedhuman.projectcreate.kamino;

import de.undefinedhuman.projectcreate.engine.observer.Event;
import de.undefinedhuman.projectcreate.kamino.annotations.Metadata;

public class BlockBreakEvent extends Event<String, String> {

    @Metadata(name = "x")
    public int x;

    protected BlockBreakEvent(Class<String> eventType, Class<String> dataType) {
        super(eventType, dataType);
    }

}
