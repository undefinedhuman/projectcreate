package de.undefinedhuman.projectcreate.server.plugin;

import de.undefinedhuman.projectcreate.engine.log.Log;

import java.io.File;

public class PluginWrapper {
    private File pluginFile;
    private Plugin plugin;
    private PluginConfig pluginConfig;
    private boolean isEnabled = false;

    private PluginWrapper() {}

    public File getPluginFile() {
        return pluginFile;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public String getName() {
        return pluginConfig.name();
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public static PluginWrapper load(File pluginFile) {
        PluginWrapper data = new PluginWrapper();
        data.pluginFile = pluginFile;
        data.pluginConfig = PluginLoader.loadPluginConfig(pluginFile);
        if(data.pluginConfig == null) {
            Log.error("Error while loading plugin.json for plugin" + pluginFile.getName());
            return null;
        }
        data.plugin = PluginLoader.loadPlugin(data.pluginConfig, pluginFile);
        if(data.plugin == null) {
            Log.error("Error while loading instance of plugin " + pluginFile.getName() + "!");
            return null;
        }
        return data;
    }

}