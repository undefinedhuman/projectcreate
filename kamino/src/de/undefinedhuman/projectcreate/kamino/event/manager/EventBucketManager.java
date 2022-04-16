package de.undefinedhuman.projectcreate.kamino.event.manager;

import de.undefinedhuman.projectcreate.engine.event.Event;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.kamino.database.Database;
import de.undefinedhuman.projectcreate.kamino.event.EventBucket;
import de.undefinedhuman.projectcreate.kamino.event.metadata.MetadataBucket;
import de.undefinedhuman.projectcreate.kamino.utils.AsyncQueue;
import de.undefinedhuman.projectcreate.kamino.utils.Compressor;

import java.util.UUID;
import java.util.concurrent.Executors;

public class EventBucketManager {

    // TODO REACT IF DATABASE FAILS TO SAVE DATA -> AND IMPLEMENT DELAY WHEN RETRYING TO SAVE THE DATA

    private final Compressor compressor;
    private final Database database;
    private final SaveProcessor saveProcessor;
    private final int maxEventsInBucket;
    private final float timeUntilBucketSave;

    private EventBucket currentBucket = new EventBucket();
    private float currentTime = 0;

    private EventBucketManager(Database database, Compressor compressor, SaveProcessor saveProcessor, float timeUntilBucketSave, int maxEventsInBucket) {
        this.database = database;
        this.compressor = compressor;
        this.timeUntilBucketSave = timeUntilBucketSave;
        this.maxEventsInBucket = maxEventsInBucket;
        this.saveProcessor = saveProcessor;
    }

    public void addEvent(Event event) {
        this.currentBucket.add(event);
    }

    public void update(float delta) {
        this.currentTime += delta;
        if(currentTime < timeUntilBucketSave && currentBucket.size() < maxEventsInBucket)
            return;
        EventBucket oldBucket = currentBucket;
        currentBucket = new EventBucket();
        currentTime = 0;
        if(oldBucket.size() == 0) return;
        saveProcessor.process(new SaveBucketTask(compressor, database, oldBucket));
    }

    public void delete() {
        this.saveProcessor.delete();
    }

    public record SaveBucketTask(Compressor compressor, Database database, EventBucket bucket) {
        public void execute() {
            long startTime = System.currentTimeMillis();
            Log.info("Start saving bucket...");
            byte[] decompressedData = bucket.serialize();
            byte[] compressedData = compressor.compress(decompressedData);
            UUID eventDataID = UUID.randomUUID();
            database.saveEventData(eventDataID.toString(), compressedData);

            UUID metadataID = UUID.randomUUID();
            MetadataBucket metadata = new MetadataBucket(eventDataID.toString(), decompressedData.length, bucket.getEvents());
            database.saveMetadata(metadataID.toString(), metadata.toJSON());
            Log.info("Saved bucket with " + bucket.size() + " events to database! Time: " + (System.currentTimeMillis() - startTime) + "ms, Compressed size: " + compressedData.length);
        }
    }

    public static Builder newInstance(Database database) {
        return new Builder(database);
    }

    public interface SaveProcessor {
        void process(SaveBucketTask task);
        void delete();
    }

    public static class Builder {
        private final Database database;
        private Compressor compressor = input -> input;
        private SaveProcessor saveProcessor;
        private int maxEventsInBucket = 5000;
        private float timeUntilBucketSave = 300f;

        public Builder(Database database) {
            this.database = database;
        }

        public Builder withMaxEventsInBucket(int amount) {
            this.maxEventsInBucket = amount;
            return this;
        }

        public Builder withTimeUntilBucketIsSaved(float seconds) {
            this.timeUntilBucketSave = seconds;
            return this;
        }

        public Builder withCompression(Compressor compressor) {
            this.compressor = compressor;
            return this;
        }

        public Builder withSaveProcessor(SaveProcessor saveProcessor) {
            this.saveProcessor = saveProcessor;
            return this;
        }

        public EventBucketManager build() {
            if(saveProcessor == null) saveProcessor = new AsyncSaveProcessor(1);
            if(timeUntilBucketSave < 1 || timeUntilBucketSave < 60) Log.warn("Time until bucket is saved needs to be in range [1, " + Integer.MAX_VALUE + "] and is recommended to be >= 60 seconds!");
            if(maxEventsInBucket < 1 || maxEventsInBucket < 100) Log.warn("Maximum amount of events per bucket needs to be larger then 1 and is recommended to be >= 100!");
            return new EventBucketManager(database, compressor, saveProcessor, Math.max(timeUntilBucketSave, 1), Math.max(maxEventsInBucket, 1));
        }

    }

    public static class AsyncSaveProcessor implements SaveProcessor {

        private final AsyncQueue queue;

        public AsyncSaveProcessor(int numberOfThreads) {
            if(numberOfThreads < 1) Log.warn("Threads used for processing bucket saves should be >= 1, wont save any log events! Proceed with caution!");
            this.queue = new AsyncQueue(Executors.newFixedThreadPool(numberOfThreads));
        }

        @Override
        public void process(SaveBucketTask task) {
            queue.add(task::execute);
        }

        @Override
        public void delete() {
            Log.info("TODO AsyncSaveProcessor delete method");
        }
    }

}