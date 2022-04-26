package de.undefinedhuman.projectcreate.kamino;

import com.badlogic.gdx.math.Vector2;
import com.playprojectcreate.kaminoapi.annotations.KaminoEvent;
import de.undefinedhuman.projectcreate.engine.config.ConfigManager;
import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.kamino.config.KaminoConfig;
import de.undefinedhuman.projectcreate.kamino.database.Couchbase;
import de.undefinedhuman.projectcreate.kamino.event.events.block.BlockBreakEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.block.BlockPlaceEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.entity.EntityDamageEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.entity.EntityDeathEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.entity.EntitySpawnEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.inventory.InventoryEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.item.ItemDropEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.player.PlayerJoinEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.player.PlayerQuitEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.shop.ShopTransactionEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.world.WorldGenerationEvent;
import de.undefinedhuman.projectcreate.kamino.event.manager.EventBucketManager;
import de.undefinedhuman.projectcreate.kamino.query.QueryUtils;
import de.undefinedhuman.projectcreate.kamino.rest.API;
import de.undefinedhuman.projectcreate.server.plugin.Plugin;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Main implements Plugin {

    private Couchbase couchbase;

    private EventBucketManager eventBucketManager;

    private static final LZ4Compressor HIGH_COMPRESSOR = LZ4Factory.fastestInstance().highCompressor();
    private static final LZ4SafeDecompressor HIGH_DECOMPRESSOR = LZ4Factory.fastestInstance().safeDecompressor();

    private API api;

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

        QueryUtils queryUtils = new QueryUtils(couchbase);

        Kamino.DATABASE = couchbase;
        Kamino.QUERY_UTILS = queryUtils;

        api = new API(couchbase, HIGH_DECOMPRESSOR::decompress, queryUtils);
        api.start();

        eventBucketManager = EventBucketManager.newInstance(couchbase)
                .withSaveProcessor(new EventBucketManager.AsyncSaveProcessor(KaminoConfig.getInstance().numberOfThreads.getValue()))
                .withMaxEventsInBucket(10000)
                .withCompression(input -> input)
                .withSaveProcessor(new EventBucketManager.AsyncSaveProcessor(1))
                .build();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("de.undefinedhuman", "com.undefinedhuman", "com.playprojectcreate"));
        Class<? extends Event>[] classes = reflections.getSubTypesOf(Event.class).stream().filter(eventClass -> eventClass.isAnnotationPresent(KaminoEvent.class)).toArray(Class[]::new);

        Kamino.registerEvents(classes);

//         Event generation estimated (not accurate real data)
//
//         Compressed Metadata + Events (10000 max events in one event bucket)
//         Player | Hours | Amount of Buckets | Disk utilization
//         1000 | 1 | 722 | 182.998MiB
//         1000 | 2 | 1354 | 366.05MiB
//         1000 | 3 | 2290 | 548.706MiB
//
//         Uncompressed Metadata + Events (10000 max events in one event bucket)
//         Player | Hours | Amount of Buckets | Disk utilization
//         1000 | 1 | 722 | 328.33MiB
//         1000 | 2 | 1354 | 656.084MiB
//         1000 | 3 | 2290 | 984.6MiB

        generateFakeData(3, 1000);

        Log.info("[kamino] Initialized successfully!");
    }

    private String[] playerUUIDs;
    private String[] worldNames;
    private final String shopID = UUID.randomUUID().toString();

    private void generateFakeData(int numberOfHours, int numberOfPlayers) {
        playerUUIDs = new String[numberOfPlayers];
        for(int i = 0; i < playerUUIDs.length; i++)
            playerUUIDs[i] = UUID.randomUUID().toString();
        worldNames = new String[numberOfPlayers/10];
        for(int i = 0; i < worldNames.length; i++)
            worldNames[i] = UUID.randomUUID().toString();
        worldNames[0] = "Main";
        for(int hour = 0; hour < numberOfHours; hour++) {
            if(Utils.isInRange(new Random().nextInt(100), 0, 10))
                eventBucketManager.addEvent(new WorldGenerationEvent(generateRandomWorldName()));
            for(int i = 0; i < 3; i++)
                if(Utils.isInRange(new Random().nextInt(100), 0, 75))
                    eventBucketManager.addEvent(new PlayerJoinEvent(null, generateRandomPlayerUUID()));
            for(int minutes = 0; minutes < 60; minutes++) {
                for(int seconds = 0; seconds < 60; seconds++) {
                    Event.DATE = new Date(
                            Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DATE),
                            8 + hour,
                            minutes,
                            seconds);
                    if(Utils.isInRange(new Random().nextInt(100), 0, 25))
                        eventBucketManager.addEvent(new EntitySpawnEvent(new Random().nextLong(playerUUIDs.length*3L), null, generateRandomWorldName()));
                    for(int i = 0; i < numberOfPlayers; i++) {
                        generateFakePlayerEvent();
                    }
                    if(Utils.isInRange(new Random().nextInt(100), 0, 25))
                        eventBucketManager.addEvent(new EntityDeathEvent(new Random().nextLong(playerUUIDs.length*3L), null, generateRandomWorldName(), 0, generateRandomPlayerUUID()));
                    eventBucketManager.update(1f);
                }
            }
            for(int i = 0; i < 3; i++)
                if(Utils.isInRange(new Random().nextInt(100), 0, 75))
                    eventBucketManager.addEvent(new PlayerQuitEvent(null, generateRandomPlayerUUID()));
        }
    }

    private void generateFakePlayerEvent() {
        int value = new Random().nextInt(100);
        if(Utils.isInRange(value, 0, 25))
            eventBucketManager.addEvent(new BlockBreakEvent(generateRandomBlockID(), generateRandomWorldName(), generateRandomPosition(), generateRandomPlayerUUID()));
        if(Utils.isInRange(value, 26, 50))
            eventBucketManager.addEvent(new BlockPlaceEvent(generateRandomBlockID(), generateRandomWorldName(), generateRandomPosition(), generateRandomPlayerUUID()));
        if(Utils.isInRange(value, 51, 60))
            eventBucketManager.addEvent(new ItemDropEvent(generateRandomBlockID(), generateRandomWorldName(), new Random().nextInt(10)));
        if(Utils.isInRange(value, 61, 70))
            eventBucketManager.addEvent(new ShopTransactionEvent(generateRandomBlockID(), generateRandomPlayerUUID(), shopID, new Random().nextInt(20), new Random().nextInt(10000)));
        if(Utils.isInRange(value, 71, 80))
            eventBucketManager.addEvent(new EntityDamageEvent(new Random().nextLong(playerUUIDs.length*3L), null, generateRandomWorldName(), generateRandomPlayerUUID(), new Random().nextLong(playerUUIDs.length*3L), generateRandomPlayerUUID()));
        if(Utils.isInRange(value, 81, 90))
            eventBucketManager.addEvent(new EntityDamageEvent(new Random().nextLong(playerUUIDs.length*3L), null, generateRandomWorldName(), new Random().nextLong(playerUUIDs.length*3L), generateRandomPlayerUUID()));
        if(Utils.isInRange(value, 91, 99))
            eventBucketManager.addEvent(new InventoryEvent(generateRandomBlockID(), generateRandomPosition(), generateRandomPlayerUUID(), generateRandomPlayerUUID()));
    }

    private Vector2 generateRandomPosition() {
        return new Vector2(new Random().nextInt(5) * 500 + new Random().nextInt(50), 1100 + new Random().nextInt(100));
    }

    private int generateRandomBlockID() {
        int value = new Random().nextInt(100);
        if(Utils.isInRange(value, 10, 20))
            return 2;
        if(Utils.isInRange(value, 21, 30))
            return 3;
        if(Utils.isInRange(value, 31, 40))
            return 4;
        if(Utils.isInRange(value, 41, 99))
            return value - 40;
        return 1;
    }

    private String generateRandomWorldName() {
        return worldNames[new Random().nextInt(worldNames.length)];
    }


    private String generateRandomPlayerUUID() {
        return playerUUIDs[new Random().nextInt(playerUUIDs.length)];
    }

    @Override
    public void update(float delta) {
        eventBucketManager.update(delta);
    }

    @Override
    public void delete() {
        api.stop();
        couchbase.close();
        Log.info("[kamino] Deleted successfully!");
    }

    //        JsonArray array = new JsonArray();
//        JsonObject blockBreakEvent = new JsonObject();
//        blockBreakEvent.addProperty("eventTypes", BlockBreakEvent.class.getCanonicalName());
//        JsonObject blockBreakEventFields = new JsonObject();
//        blockBreakEventFields.addProperty("blockIDs", "992");
//        JsonObject area = new JsonObject();
//        area.add("min", new Gson().toJsonTree(new Vector2(0, 0)));
//        area.add("max", new Gson().toJsonTree(new Vector2(1500, 1500)));
//        blockBreakEventFields.add("area", area);
//        blockBreakEvent.add("fields", blockBreakEventFields);
//        array.add(blockBreakEvent);
//
//        JsonObject blockBreakEvent2 = new JsonObject();
//        blockBreakEvent2.addProperty("eventTypes", BlockBreakEvent.class.getCanonicalName());
//        JsonObject blockBreakEventFields2 = new JsonObject();
//        //blockBreakEventFields2.addProperty("worldNames", "Main");
//        blockBreakEvent2.add("fields", blockBreakEventFields2);
//        //array.add(blockBreakEvent2);
//
//        JsonObject playerJoinEvent = new JsonObject();
//        playerJoinEvent.addProperty("eventTypes", PlayerJoinEvent.class.getCanonicalName());
//        // array.add(playerJoinEvent);
//
//        Log.info(array);
//
//        Log.info(QueryEndpoint.parseRequest(couchbase, array, HIGH_DECOMPRESSOR::decompress, false).size());

}
