package de.undefinedhuman.projectcreate.server.plugin;

import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginManager implements Manager {

    private ArrayList<Plugin> plugins = new ArrayList<>();
    private List<Plugin> unmodifiablePlugins = Collections.unmodifiableList(plugins);

    @Override
    public void init() {
    }

    @Override
    public void delete() {
    }

    public void loadPlugins(FsFile... pluginFiles) {

    }

}
