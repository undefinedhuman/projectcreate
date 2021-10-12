package de.undefinedhuman.projectcreate.engine.utils.ds;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.function.Supplier;

public class ObjectPool<T extends Poolable> {

    private long timeUntilDeletion;

    private Supplier<T> supplier;
    private Hashtable<T, Long> objects;

    public ObjectPool(Supplier<T> supplier) {
        this(supplier, 60000);
    }

    public ObjectPool(Supplier<T> supplier, long timeUntilDeletion) {
        this.timeUntilDeletion = timeUntilDeletion;
        this.supplier = supplier;
        objects = new Hashtable<>();
    }

    public synchronized void add(T object) {
        this.add(object, System.currentTimeMillis());
    }

    public synchronized void add(ArrayList<T> objects) {
        long currentTime = System.currentTimeMillis();
        for(T object : objects)
            add(object, currentTime);
    }

    private synchronized void add(T object, long time) {
        object.freeUp();
        this.objects.put(object, time);
    }

    public synchronized T get() {
        long currentTime = System.currentTimeMillis();
        if(objects.size() == 0)
            return createNewObject();
        T currentObject;
        Enumeration<T> e = objects.keys();
        while (e.hasMoreElements()) {
            currentObject = e.nextElement();
            if ((currentTime - objects.get(currentObject)) > timeUntilDeletion)
                removeObject(currentObject);
            else {
                if (currentObject.validate()) {
                    objects.remove(currentObject);
                    return currentObject;
                } else removeObject(currentObject);
            }
        }
        return createNewObject();
    }

    public void delete() {
        for(T object : objects.keySet())
            object.delete();
        objects.clear();
    }

    private T createNewObject() {
        T object = supplier.get();
        object.init();
        return object;
    }

    private void removeObject(T object) {
        object.delete();
        objects.remove(object);
    }

    public Hashtable<T, Long> getObjects() {
        return objects;
    }
}
