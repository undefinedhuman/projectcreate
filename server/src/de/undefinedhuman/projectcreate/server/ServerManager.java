package de.undefinedhuman.projectcreate.server;

import com.esotericsoftware.kryonet.Server;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.NetworkConstants;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationListener;
import de.undefinedhuman.projectcreate.engine.language.LanguageManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.resources.font.FontManager;
import de.undefinedhuman.projectcreate.engine.resources.sound.SoundManager;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

import java.io.IOException;

public class ServerManager extends HeadlessApplicationListener {

    private static volatile ServerManager instance;

    private Server server;
    private ManagerList managerList;

    public ServerManager() {
        this.server = new Server(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        NetworkConstants.register(server);

        managerList = new ManagerList();
        managerList.addManager(
                Log.getInstance().setLogMessageDecorator(
                        new LogMessage()
                                .andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT))
                                .andThen(value -> LogMessageDecorators.withModuleName(value, "Game"))
                ),
                ConfigManager.getInstance().setConfigs(ServerConfig.getInstance()),
                LanguageManager.getInstance(),
                TextureManager.getInstance(),
                SoundManager.getInstance(),
                FontManager.getInstance(),
                BlueprintManager.getInstance(),
                ItemManager.getInstance());
    }

    @Override
    public void create() {
        try {
            server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {
        server.stop();
    }

    public static ServerManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (ServerManager.class) {
            if (instance == null)
                instance = new ServerManager();
        }
        return instance;
    }

}
