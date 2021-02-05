package de.undefinedhuman.sandboxgame.engine.utils;

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool<T> {

    private long timeUntilDeletion;

    private Hashtable<T, Long> objects;

    public ObjectPool() {
        this(60000);
    }

    public ObjectPool(long timeUntilDeletion) {
        this.timeUntilDeletion = timeUntilDeletion;
        objects = new Hashtable<>();
    }

    public void delete() {
        for(T object : objects.keySet())
            delete(object);
        objects.clear();
    }

    protected abstract T createInstance();

    public abstract boolean validate(T object);

    public abstract void delete(T object);

    public synchronized void add(T object) {
        objects.put(object, System.currentTimeMillis());
    }

    public synchronized T get() {
        long currentTime = System.currentTimeMillis();
        if(objects.size() == 0)
            return createNewObject(currentTime);
        T currentObject;
        Enumeration<T> e = objects.keys();
        while (e.hasMoreElements()) {
            currentObject = e.nextElement();
            if ((currentTime - objects.get(currentObject)) > timeUntilDeletion)
                removeObject(currentObject);
            else {
                if (validate(currentObject)) {
                    objects.remove(currentObject);
                    return currentObject;
                } else removeObject(currentObject);
            }
        }
        return createNewObject(currentTime);
    }

    private T createNewObject(long time) {
        T object = createInstance();
        objects.put(object, time);
        return object;
    }

    private void removeObject(T object) {
        delete(object);
        objects.remove(object);
    }

}
