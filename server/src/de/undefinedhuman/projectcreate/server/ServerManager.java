package de.undefinedhuman.projectcreate.server;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import de.undefinedhuman.projectcreate.core.ecs.ComponentTypes;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.system.MovementSystem;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.network.log.NetworkLogger;
import de.undefinedhuman.projectcreate.core.network.packets.CommandCache;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.utils.NetworkConstants;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessage;
import de.undefinedhuman.projectcreate.engine.log.decorator.LogMessageDecorators;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.engine.utils.Timer;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.server.config.ServerConfig;
import de.undefinedhuman.projectcreate.server.network.PlayerConnection;
import de.undefinedhuman.projectcreate.server.network.ServerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ServerManager extends Server {

    private static volatile ServerManager instance;

    private final ManagerList managers = new ManagerList();
    private final ArrayList<Timer> timers = new ArrayList<>();

    private Scanner consoleInput;

    public ArrayList<CommandCache> COMMAND_CACHE = new ArrayList<>();

    public ServerManager() {
        super(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        NetworkConstants.register(this);

        consoleInput = new Scanner(System.in);

        managers.addManager(
                Log.getInstance().setLogMessageDecorator(
                        new LogMessage()
                                .andThen(value -> LogMessageDecorators.withDate(value, Variables.LOG_DATE_FORMAT))
                                .andThen(value -> LogMessageDecorators.withModuleName(value, "Server"))
                ),
                ConfigManager.getInstance().setConfigs(ServerConfig.getInstance()),
                BlueprintManager.getInstance(),
                EntityManager.getInstance(),
                ItemManager.getInstance()
        );

        addTimers(
                new Timer(0.2f, () -> Arrays.stream(getConnections()).forEach(Connection::updateReturnTripTime)),
                new Timer(0.25f, () -> {
                    if(!EntityManager.getInstance().stream().findAny().isPresent())
                        return;
                    Log.info(EntityManager.getInstance().stream().map(longEntityEntry -> {
                        Entity entity = longEntityEntry.getValue();
                        return Mappers.TRANSFORM.get(entity).getPosition();
                    }).collect(Collectors.toList()).toString());
                }),
                new Timer(900f, () -> Log.getInstance().save())
        );

        addListener(new ServerListener());
    }

    public void init() {
        initGDX();
        ComponentTypes.registerComponentTypes(BlueprintManager.getInstance(), AnimationBlueprint.class, SpriteBlueprint.class, InteractionBlueprint.class);
        EntityManager.getInstance().addSystems(new MovementSystem(false) {
            @Override
            public float getLatency() {
                return 0;
            }
        });
        managers.init();
        try {
            bind(ServerConfig.getInstance().TCP_PORT.getValue(), NetworkConstants.DEFAULT_UDP_PORT);
        } catch (IOException ex) {
            Log.error("Error while opening the tcp and udp port", ex);
        }
        start();
    }

    public void update(float delta) {
        timers.forEach(timer -> timer.update(delta));
        EntityManager.getInstance().update(delta);
        COMMAND_CACHE.forEach(CommandCache::process);
        COMMAND_CACHE.clear();
        EntityManager.getInstance().stream().map(Map.Entry::getValue).forEach(entity -> {
            PositionPacket packet = PositionPacket.serialize(entity);
            sendToAllUDP(packet);
        });
    }

    public void delete() {
        consoleInput.close();
        consoleInput = null;
        stop();
        timers.forEach(Timer::delete);
        timers.clear();
        managers.delete();
        System.exit(0);
    }

    public void addTimers(Timer... timers) {
        this.timers.addAll(Arrays.asList(timers));
    }

    private void readInput() {
        if(consoleInput == null)
            return;
        String input = consoleInput.nextLine();
        if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("stop"))
            delete();
    }

    private void initGDX() {
        Gdx.app.setApplicationLogger(Log.getInstance());
        Gdx.app.setLogLevel(Variables.LOG_LEVEL.ordinal());
        com.esotericsoftware.minlog.Log.setLogger(new NetworkLogger());
    }

    @Override
    protected Connection newConnection() {
        return new PlayerConnection();
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
