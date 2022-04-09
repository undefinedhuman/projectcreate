package de.undefinedhuman.projectcreate.kamino.utils;

import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncQueue {

    private final List<Runnable> runnable = new LinkedList<>();
    private final Executor executor;

    public AsyncQueue(Executor executor) {
        this.executor = executor;
    }

    public void add(Runnable runnable) {
        synchronized(this.runnable) { this.runnable.add(runnable); }
        update();
    }

    public int size() {
        synchronized(runnable) { return runnable.size(); }
    }

    public Runnable get(int index) {
        synchronized(runnable) { return runnable.get(index); }
    }

    private void update() {
        synchronized(runnable) {
            if(runnable.isEmpty()) return;
            executor.execute(new RunnableWrapper(runnable.remove(0), this::update));
        }
    }

    record RunnableWrapper(Runnable runnable, Runnable callback) implements Runnable {
        public void run() {
            try {
                runnable.run();
            } catch (Exception ex) {
                Log.error("Error while running task of queue!", ex);
            } finally {
                callback.run();
            }
        }
    }

}