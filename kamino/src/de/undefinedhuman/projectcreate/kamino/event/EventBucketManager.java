package de.undefinedhuman.projectcreate.kamino.event;

import com.badlogic.gdx.utils.Queue;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class EventBucketManager {

    private static final long BUCKET_TIME_UNTIL_SERIALIZATION = 60 * 60 * 5;

    private EventBucket currentBucket = new EventBucket();
    private final Queue<EventBucket> bucketsToSerialize = new Queue<>();
    private int currentTime = 0;

    public void addEvent(Event event) {
        this.currentBucket.add(event);
    }

    public void update(float delta) {
        this.currentTime += delta;
        if(currentTime >= BUCKET_TIME_UNTIL_SERIALIZATION) {
            bucketsToSerialize.addLast(currentBucket);
            currentBucket = new EventBucket();
        }

    }

    private void serializeBucket(EventBucket bucket) {

    }

}
