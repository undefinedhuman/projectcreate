package de.undefinedhuman.projectcreate.core.network.buffer;

import java.util.LinkedList;
import java.util.Queue;

public class NetworkBuffer {

    private Queue<BufferObject> objects;

    public NetworkBuffer() {
        objects = new LinkedList<>();
    }

    public synchronized void add(BufferObject object) {
        this.objects.add(object);
    }

    public void process() {
        int size = objects.size();
        for(int i = 0; i < size; i++) {
            BufferObject object = objects.poll();
            if(object != null)
                object.process();
        }
    }

}
