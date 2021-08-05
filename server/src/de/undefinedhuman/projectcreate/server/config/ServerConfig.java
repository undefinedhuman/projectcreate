package de.undefinedhuman.projectcreate.server.config;

import de.undefinedhuman.projectcreate.core.network.utils.NetworkConstants;
import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class ServerConfig extends Config {

    private static volatile ServerConfig instance;
    private static final ServerConfigValidator VALIDATOR = new ServerConfigValidator();

    public final IntSetting
            TCP_PORT = new IntSetting("tcpPort", NetworkConstants.DEFAULT_TCP_PORT, 1, 65535);

    private ServerConfig() {
        super("server");
        addSettings(TCP_PORT);
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
