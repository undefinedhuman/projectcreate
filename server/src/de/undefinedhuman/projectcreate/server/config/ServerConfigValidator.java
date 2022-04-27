package de.undefinedhuman.projectcreate.server.config;

import de.undefinedhuman.projectcreate.core.network.utils.NetworkConstants;
import de.undefinedhuman.projectcreate.engine.config.ConfigValidator;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;

public class ServerConfigValidator extends ConfigValidator<ServerConfig> {

    public ServerConfigValidator() {
        super(
                new ValidationRule<>(
                        serverConfig -> Utils.isInRange(serverConfig.TCP_PORT.getValue(), 1, 65535),
                        "TCP port must be in range of [1, 65535], change back to " + NetworkConstants.DEFAULT_TCP_PORT,
                        serverConfig -> serverConfig.TCP_PORT.setValue(NetworkConstants.DEFAULT_TCP_PORT)
                ),
                new ValidationRule<>(
                        serverConfig -> Utils.isInRange(serverConfig.TCP_PORT.getValue(), 1024, 65535),
                        "TCP port should be in the range of [1024, 65535], otherwise you better know what you are doing!",
                        serverConfig -> {}
                )
        );
    }

}
