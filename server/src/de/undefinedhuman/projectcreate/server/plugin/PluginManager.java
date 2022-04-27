package de.undefinedhuman.projectcreate.server.plugin;

import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class PluginManager implements Manager {

    private static final FilenameFilter JAR_FILE_FILTER = (dir, name) -> name.trim().length() > 4 && name.endsWith(".jar");

    private FsFile pluginDirectory;
    private final HashMap<String, PluginWrapper> plugins = new HashMap<>();
    private final Collection<PluginWrapper> unmodifiablePlugins = Collections.unmodifiableCollection(plugins.values());

    public PluginManager() {
        this(new FsFile(Paths.getInstance().getDirectory(), Paths.PLUGIN_PATH));
    }

    public PluginManager(FsFile directory) {
        this.pluginDirectory = directory;
        if(!pluginDirectory.exists() || !pluginDirectory.isDirectory())
            (pluginDirectory = new FsFile(Paths.getInstance().getDirectory(), Paths.PLUGIN_PATH)).mkdirs();
    }

    @Override
    public void init() {
        if(pluginDirectory == null || !pluginDirectory.exists() || !pluginDirectory.isDirectory()) {
            Log.error("Error while loading and/or creating plugins directory!");
            return;
        }
        loadPlugins(pluginDirectory);
        validatePlugins();
        initPlugins();
    }

    @Override
    public void update(float delta) {
        unmodifiablePlugins.forEach(pluginWrapper -> pluginWrapper.getPlugin().update(delta));
    }

    @Override
    public void delete() {
        deletePlugins();
        plugins.clear();
    }

    public Collection<PluginWrapper> getPlugins() {
        return unmodifiablePlugins;
    }

    private void loadPlugins(FsFile pluginsDirectory) {
        Arrays.stream(pluginsDirectory.list(JAR_FILE_FILTER))
                .forEach(pluginFile -> {
                    PluginWrapper pluginData = PluginWrapper.load(pluginFile.file());
                    if(pluginData == null || plugins.containsKey(pluginData.getName())) return;
                    plugins.put(pluginData.getName(), pluginData);
                });
    }

    private void validatePlugins() {
        Log.warn("TODO PLUGIN VALIDATION! DEPENDENCY CHECK VERSION, DEPENDENCY VERSION CHECKS, SERVER VERSION CHECKS");
        unmodifiablePlugins.forEach(pluginWrapper -> pluginWrapper.setEnabled(true));
    }

    private void initPlugins() {
        unmodifiablePlugins.forEach(pluginWrapper -> {
            if(!pluginWrapper.isEnabled()) return;
            pluginWrapper.getPlugin().init();
        });
    }

    private void deletePlugins() {
        unmodifiablePlugins.forEach(pluginWrapper -> {
            if(!pluginWrapper.isEnabled()) return;
            pluginWrapper.getPlugin().delete();
        });
    }

}
