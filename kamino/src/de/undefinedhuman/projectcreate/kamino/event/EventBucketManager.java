package de.undefinedhuman.projectcreate.kamino.event;

import com.badlogic.gdx.utils.Queue;
import de.undefinedhuman.projectcreate.engine.event.Event;

public class EventBucketManager {

    private static final float BUCKET_TIME_UNTIL_SAVE = 60f * 5f;

    private EventBucket currentBucket = new EventBucket();
    private final Queue<EventBucket> bucketsToSerialize = new Queue<>();
    private float currentTime = 0;

    public void addEvent(Event event) {
        this.currentBucket.add(event);
    }

    public void update(float delta) {
        this.currentTime += delta;
        if(currentTime < BUCKET_TIME_UNTIL_SAVE)
            return;
        EventBucket oldBucket = currentBucket;
        currentBucket = new EventBucket();
        bucketsToSerialize.addLast(oldBucket);
    }

    private void serializeBucket(EventBucket bucket) {

    }

}
