package de.undefinedhuman.projectcreate.server.plugin;

import java.io.File;

public abstract class Plugin {

    private File pluginFile;
    private boolean isEnabled = false;

    public Plugin() {

    }

    public Plugin(File file) {
        this.pluginFile = file;
    }

    boolean isEnabled() {
        return isEnabled;
    }

    public abstract void load();
    public abstract void init();
    public abstract void delete();

}
