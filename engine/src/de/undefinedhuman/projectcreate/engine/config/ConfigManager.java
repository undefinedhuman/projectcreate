package de.undefinedhuman.projectcreate.engine.config;

import de.undefinedhuman.projectcreate.engine.file.Serializable;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

import java.util.Arrays;
import java.util.List;

public class ConfigManager extends Manager implements Serializable {

    public static ConfigManager instance;

    private List<Config> configs;

    public ConfigManager(Config... configs) {
        if (instance == null)
            instance = this;
        this.configs = Arrays.asList(configs);
    }

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
        configs.forEach(Config::load);
    }

}
