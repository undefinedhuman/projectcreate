package com.playprojectcreate.testplugin;

import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import com.playprojectcreate.kaminoapi.annotations.Metadata;
import de.undefinedhuman.projectcreate.engine.event.Event;

@KaminoEvent
public class TestPluginEvent extends Event {

    @Metadata
    public final String playerID;

    public TestPluginEvent(String playerID) {
        this.playerID = playerID;
    }
}
