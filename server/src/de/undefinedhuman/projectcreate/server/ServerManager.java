package de.undefinedhuman.projectcreate.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import de.undefinedhuman.projectcreate.core.ecs.ComponentTypes;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.buffer.NetworkBuffer;
import de.undefinedhuman.projectcreate.core.network.log.NetworkLogger;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.utils.NetworkConstants;
import de.undefinedhuman.projectcreate.core.utils.ApplicationLoggerAdapter;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.manager.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.timer.Timer;
import de.undefinedhuman.projectcreate.engine.utils.timer.TimerAction;
import de.undefinedhuman.projectcreate.engine.utils.timer.TimerList;
import de.undefinedhuman.projectcreate.server.config.ServerConfig;
import de.undefinedhuman.projectcreate.server.entity.MovementSystem;
import de.undefinedhuman.projectcreate.server.network.PlayerConnection;
import de.undefinedhuman.projectcreate.server.network.ServerListener;
import de.undefinedhuman.projectcreate.server.utils.commands.CommandManager;
import de.undefinedhuman.projectcreate.server.utils.console.Console;

import java.io.IOException;
import java.util.Arrays;

public class ServerManager extends Server {

    private static volatile ServerManager instance;

    private final ManagerList managers = new ManagerList();
    private final TimerList timers = new TimerList();

    private NetworkBuffer buffer;

    private ServerManager() {
        super(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        buffer = new NetworkBuffer();
        NetworkConstants.registerPackets(this);

        managers.addManager(
                Log.getInstance().setLogMessageDecorator(
                        new LogMessage()
                                .andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT))
                                .andThen(value -> LogMessageDecorators.withModuleName(value, "Server"))
                ),
                ConfigManager.getInstance().setConfigs(ServerConfig.getInstance()),
                BlueprintManager.getInstance(),
                EntityManager.getInstance(),
                ItemManager.getInstance(),
                CommandManager.getInstance()
                        .addCommand("stop", (sender, label, args) -> {
                            delete();
                            return true;
                        }),
                Console.getInstance()
        );

        initTimers();

        addListener(ServerListener.getInstance());
    }

    public void init() {
        initGDX();
        ComponentTypes.registerComponentTypes(BlueprintManager.getInstance(), AnimationBlueprint.class, SpriteBlueprint.class, InteractionBlueprint.class);
        EntityManager.getInstance().addSystems(new MovementSystem());
        managers.init();
        try {
            bind(ServerConfig.getInstance().TCP_PORT.getValue(), NetworkConstants.DEFAULT_UDP_PORT);
        } catch (IOException ex) {
            Log.error("Error while opening the tcp and udp port", ex);
        }
        start();
    }

    public void update(float delta) {
        timers.update(delta);
        EntityManager.getInstance().update(delta);
        buffer.process();
        EntityManager.getInstance().setUpdating(true);
        EntityManager.getInstance().getEntities().forEach(entity -> sendToAllUDP(PositionPacket.serialize(entity)));
        EntityManager.getInstance().setUpdating(false);
    }

    public void delete() {
        buffer.process();
        stop();
        timers.delete();
        managers.delete();
        System.exit(0);
    }

    private void initGDX() {
        Gdx.app.setApplicationLogger(ApplicationLoggerAdapter.getInstance());
        Gdx.app.setLogLevel(Variables.LOG_LEVEL.ordinal());
        NetworkLogger.setLogger();
    }

    private void initTimers() {
        addTimer(0.2f, () -> Arrays.stream(getConnections()).forEach(Connection::updateReturnTripTime));
        addTimer(900f, () -> Log.getInstance().save());
    }

    private void addTimer(float time, TimerAction action) {
        timers.addTimers(new Timer(time, action));
    }

    @Override
    protected Connection newConnection() {
        return new PlayerConnection();
    }

    public NetworkBuffer getBuffer() {
        return buffer;
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
