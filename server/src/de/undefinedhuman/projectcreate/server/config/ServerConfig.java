package de.undefinedhuman.projectcreate.server.config;

import de.undefinedhuman.projectcreate.core.network.utils.NetworkConstants;
import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.EnumSetting;
import de.undefinedhuman.projectcreate.server.ServerManager;

public class ServerConfig extends Config {

    private static volatile ServerConfig instance;
    private static final ServerConfigValidator VALIDATOR = new ServerConfigValidator();

    public final IntSetting TCP_PORT = new IntSetting("port", NetworkConstants.DEFAULT_TCP_PORT, 1, 65535);
    public final EnumSetting<Level> LOG_LEVEL = new EnumSetting<>("loglevel", Level.INFO, Level.values(), Level::valueOf);

    private ServerConfig() {
        super("server", false);
        addSettings(TCP_PORT, LOG_LEVEL);
        LOG_LEVEL.addValueListener(value -> ServerManager.getInstance().setLogLevel(value));
    }

    public static ServerConfig getInstance() {
        if(instance != null)
            return instance;
        if (instance == null) {
            synchronized (ServerConfig.class) {
                if (instance == null)
                    instance = new ServerConfig();
            }
        }
        return instance;
    }

    @Override
    public void validate() {
        VALIDATOR.validate(this);
    }
}
