package com.playprojectcreate.testplugin;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.server.plugin.Plugin;

public class Main implements Plugin {

    public Main() {
        Log.info("TEST-PLUGIN CONSTRUCTOR");
    }

    @Override
    public void init() {
        Log.info("[TestPlugin] Initialized successfully!");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void delete() {
        Log.info("[TestPlugin] Deleted successfully!");
    }
}
