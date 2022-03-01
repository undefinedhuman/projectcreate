package de.undefinedhuman.projectcreate.engine.config;

import de.undefinedhuman.projectcreate.engine.file.Serializable;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigManager implements Manager, Serializable {

    private static volatile ConfigManager instance;

    private final List<Config> configs = new ArrayList<>();

    private ConfigManager() {}

    @Override
    public void init() {
        configs.forEach(Config::init);
    }

    @Override
    public void delete() {
        configs.forEach(Config::delete);
    }

    @Override
    public void save() {
        configs.forEach(Config::save);
    }

    @Override
    public void load() {
        configs.forEach(config -> {
            config.load();
            config.validate();
        });
    }

    public ConfigManager setConfigs(Config... configs) {
        this.configs.clear();
        Collections.addAll(this.configs, configs);
        return this;
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null)
                    instance = new ConfigManager();
            }
        }
        return instance;
    }

}
