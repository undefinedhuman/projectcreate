package de.undefinedhuman.projectcreate.engine.config;

import de.undefinedhuman.projectcreate.engine.file.Serializable;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.*;

public class ConfigManager implements Manager, Serializable {

    private static volatile ConfigManager instance;

    private final HashMap<Class<? extends Config>, Config> configs = new HashMap<>();
    private final Collection<Config> unmodifiableConfigs = Collections.unmodifiableCollection(configs.values());

    private ConfigManager() {}

    @Override
    public void delete() {
        unmodifiableConfigs.forEach(Config::delete);
    }

    @Override
    public void save() {
        unmodifiableConfigs.forEach(Config::save);
    }

    @Override
    public void load() {
        unmodifiableConfigs.forEach(config -> {
            config.load();
            config.validate();
        });
    }

    public <T extends Config> T getConfig(Class<T> configClass) {
        return (T) this.configs.get(configClass);
    }

    public ConfigManager addConfigs(Config... configs) {
        for(Config config : configs) {
            config.init();
            this.configs.put(config.getClass(), config);
        }
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
