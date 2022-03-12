package de.undefinedhuman.projectcreate.server.plugin;

import java.io.File;

public abstract class Plugin {
    private File pluginFile;
    private PluginConfig pluginConfig;
    private boolean isEnabled = false;
    private Plugin plugin;

    public Plugin() {

    }

    public Plugin(File file) {
        this.pluginFile = file;
    }

    boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public abstract void load();
    public abstract void init();
    public abstract void delete();
}
