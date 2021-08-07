package de.undefinedhuman.projectcreate.server.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IDManager {

    private static volatile IDManager instance;

    private AtomicLong currentMaxWorldID = new AtomicLong();

    private IDManager() {}

    public long getCurrentMaxWorldID() {
        return currentMaxWorldID.getAndIncrement();
    }

    public static IDManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (IDManager.class) {
            if (instance == null)
                instance = new IDManager();
        }
        return instance;
    }
}
