package de.undefinedhuman.projectcreate.kamino;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.kamino.config.KaminoConfig;
import de.undefinedhuman.projectcreate.kamino.database.Couchbase;
import de.undefinedhuman.projectcreate.kamino.event.events.BlockBreakEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.PlayerJoinEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.PlayerQuitEvent;
import de.undefinedhuman.projectcreate.kamino.event.manager.EventBucketManager;
import de.undefinedhuman.projectcreate.kamino.query.QueryEndpoint;
import de.undefinedhuman.projectcreate.kamino.rest.APIKt;
import de.undefinedhuman.projectcreate.server.plugin.Plugin;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Random;

public class Main implements Plugin {

    private Couchbase couchbase;

    private EventBucketManager eventBucketManager;

    private static final LZ4Compressor HIGH_COMPRESSOR = LZ4Factory.fastestInstance().highCompressor();
    private static final LZ4SafeDecompressor HIGH_DECOMPRESSOR = LZ4Factory.fastestInstance().safeDecompressor();

    @Override
    public void init() {
        ConfigManager.getInstance().addConfigs(KaminoConfig.getInstance());
        couchbase = new Couchbase.Builder(
                KaminoConfig.getInstance().databaseUrl.getValue(),
                KaminoConfig.getInstance().databaseUser.getValue(),
                KaminoConfig.getInstance().databasePassword.getValue(),
                KaminoConfig.getInstance().bucketName.getValue())
                .build();
        couchbase.init();

        APIKt.start();

        eventBucketManager = EventBucketManager.newInstance(couchbase)
                .withSaveProcessor(new EventBucketManager.AsyncSaveProcessor(KaminoConfig.getInstance().numberOfThreads.getValue()))
                .withCompression(HIGH_COMPRESSOR::compress)
                .build();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("de.undefinedhuman", "com.undefinedhuman", "com.playprojectcreate"));
        Class<? extends Event>[] classes = reflections.getSubTypesOf(Event.class).stream().filter(eventClass -> eventClass.isAnnotationPresent(KaminoEvent.class)).toArray(Class[]::new);

        Kamino.registerEvents(classes);

        JsonArray array = new JsonArray();
        JsonObject blockBreakEvent = new JsonObject();
        blockBreakEvent.addProperty("eventTypes", BlockBreakEvent.class.getCanonicalName());
        JsonObject blockBreakEventFields = new JsonObject();
        blockBreakEventFields.addProperty("worldNames", "Main");
        JsonObject area = new JsonObject();
        area.add("min", new Gson().toJsonTree(new Vector2(1, 1)));
        area.add("max", new Gson().toJsonTree(new Vector2(10, 10)));
        // blockBreakEventFields.add("area", area);
        blockBreakEvent.add("fields", blockBreakEventFields);
        // array.add(blockBreakEvent);

        JsonObject blockBreakEvent2 = new JsonObject();
        blockBreakEvent2.addProperty("eventTypes", BlockBreakEvent.class.getCanonicalName());
        JsonObject blockBreakEventFields2 = new JsonObject();
        blockBreakEventFields2.addProperty("blockIDs", 992);
        blockBreakEvent2.add("fields", blockBreakEventFields2);
        array.add(blockBreakEvent2);

        JsonObject playerJoinEvent = new JsonObject();
        playerJoinEvent.addProperty("eventTypes", PlayerJoinEvent.class.getCanonicalName());
        // array.add(playerJoinEvent);

        Log.info(array);

        Log.info(QueryEndpoint.parseRequest(couchbase, array, HIGH_DECOMPRESSOR::decompress, false).size());

        Log.info("[kamino] Initialized successfully!");
    }

    private void insertFakeData() {
        for(int i = 0; i < 3; i++) {
            eventBucketManager.addEvent(new BlockBreakEvent(new Random().nextInt(1000), "Main", new Vector2(i, i)));
            eventBucketManager.addEvent(new BlockBreakEvent(new Random().nextInt(1000), "Test", new Vector2(i, i)));
            eventBucketManager.addEvent(new PlayerJoinEvent("UUID" + new Random().nextInt(100)));
            eventBucketManager.addEvent(new PlayerQuitEvent("UUID" + new Random().nextInt(100)));
        }
        eventBucketManager.update(300);

        eventBucketManager.addEvent(new PlayerJoinEvent("UUID1"));
        eventBucketManager.update(300);

        eventBucketManager.addEvent(new PlayerJoinEvent("UUID2"));
        eventBucketManager.update(300);

        eventBucketManager.addEvent(new PlayerJoinEvent("UUID1"));
        eventBucketManager.addEvent(new PlayerJoinEvent("UUID2"));
        eventBucketManager.update(300);

        eventBucketManager.addEvent(new PlayerJoinEvent("UUID1"));
        eventBucketManager.addEvent(new PlayerJoinEvent("UUID2"));
        eventBucketManager.addEvent(new PlayerJoinEvent("UUID3"));
        eventBucketManager.update(300);

        eventBucketManager.addEvent(new PlayerJoinEvent("UUID2"));
        eventBucketManager.addEvent(new PlayerJoinEvent("UUID3"));
        eventBucketManager.update(300);

        eventBucketManager.addEvent(new PlayerJoinEvent("UUID1"));
        eventBucketManager.addEvent(new PlayerJoinEvent("UUID3"));
        eventBucketManager.update(300);
    }

    @Override
    public void update(float delta) {
        eventBucketManager.update(delta);
    }

    @Override
    public void delete() {
        APIKt.stop();
        couchbase.close();
        Log.info("[kamino] Deleted successfully!");
    }

}
