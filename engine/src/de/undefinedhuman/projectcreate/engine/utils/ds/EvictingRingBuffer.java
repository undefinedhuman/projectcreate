package de.undefinedhuman.projectcreate.engine.utils.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EvictingRingBuffer<T> implements Iterable<T> {

    private T[] data;
    private int count = 0;
    private int indexOut = 0;
    private int indexIn = 0;

    public EvictingRingBuffer(int capacity) {
        data = (T[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void push(T item) {
        if (count == data.length) {
            throw new RuntimeException("Ring buffer overflow");
        }
        data[indexIn] = item;
        indexIn = (indexIn + 1) % data.length;     // wrap-around
        count++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T item = data[indexOut];
        data[indexOut] = null;                  // to help with garbage collection
        count--;
        indexOut = (indexOut + 1) % data.length; // wrap-around
        return item;
    }

    public Iterator<T> iterator() {
        return new RingBufferIterator();
    }

    private class RingBufferIterator implements Iterator<T> {

        private int i = 0;

        public boolean hasNext() {
            return i < count;
        }

        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return data[i++];
        }
    }

}
