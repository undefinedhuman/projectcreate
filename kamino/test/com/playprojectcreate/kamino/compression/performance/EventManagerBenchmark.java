package com.playprojectcreate.kamino.compression.performance;

import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.kamino.database.Database;
import de.undefinedhuman.projectcreate.kamino.event.events.block.BlockBreakEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.player.PlayerJoinEvent;
import de.undefinedhuman.projectcreate.kamino.event.events.player.PlayerQuitEvent;
import de.undefinedhuman.projectcreate.kamino.event.manager.EventBucketManager;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import java.util.Random;

public class EventManagerBenchmark {

    // TODO PROPER BENCHMARKING

    private static long NUMBER_OF_ITERATIONS = 1000;
    private static long BUCKET_SIZE = 0;

    public static void main(String[] args) {

        Log.getInstance().setLogLevel(Level.WARN);

        Database database = new Database() {
            @Override
            public void init() {}

            @Override
            public boolean saveMetadata(String id, String json) {
                return false;
            }

            @Override
            public boolean saveEventData(String id, byte[] data) {
                BUCKET_SIZE = data.length;
                return false;
            }

            @Override
            public void close() {}
        };

        LZ4Compressor HIGH_COMPRESSOR = LZ4Factory.fastestInstance().highCompressor();
        EventBucketManager highCompressorManager = new EventBucketManager.Builder(database)
                .withSaveProcessor(new EventBucketManager.SaveProcessor() {
                    @Override
                    public void process(EventBucketManager.SaveBucketTask task) {
                        task.execute();
                    }

                    @Override
                    public void delete() {}
                })
                .withMaxEventsInBucket(100)
                .withCompressor(HIGH_COMPRESSOR::compress)
                .build();
        benchmarkManager(highCompressorManager, NUMBER_OF_ITERATIONS);

        LZ4Compressor FAST_COMPRESSOR = LZ4Factory.fastestInstance().fastCompressor();
        EventBucketManager fastCompressorManager = new EventBucketManager.Builder(database)
                .withSaveProcessor(new EventBucketManager.SaveProcessor() {
                    @Override
                    public void process(EventBucketManager.SaveBucketTask task) {
                        task.execute();
                    }

                    @Override
                    public void delete() {}
                })
                .withMaxEventsInBucket(100)
                .withCompressor(FAST_COMPRESSOR::compress)
                .build();
        benchmarkManager(fastCompressorManager, NUMBER_OF_ITERATIONS);
    }

    private static void benchmarkManager(EventBucketManager eventBucketManager, long numberOfIterations) {
        long sumOfSize = 0;
        long sumOfTime = 0;
        for(int i = 0; i < numberOfIterations; i++) {
            long currentMillis = System.currentTimeMillis();
            addEventsToManager(eventBucketManager);
            eventBucketManager.update(0f);
            sumOfTime += System.currentTimeMillis() - currentMillis;
            sumOfSize += BUCKET_SIZE;
        }
        Log.warn("Iterations: " + numberOfIterations, "Size: " + (sumOfSize/numberOfIterations), "Time: " + (sumOfTime/numberOfIterations));
    }

    private static void addEventsToManager(EventBucketManager manager) {
        for(int i = 0; i < 1500; i++) {
            manager.addEvent(new BlockBreakEvent(new Random().nextInt(1000), "Main", null));
            manager.addEvent(new PlayerJoinEvent("UUID" + new Random().nextInt(100)));
            manager.addEvent(new PlayerQuitEvent("UUID" + new Random().nextInt(100)));
        }
    }

}
