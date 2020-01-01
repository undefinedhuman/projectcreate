package de.undefinedhuman.sandboxgameserver.utils.threading;

import de.undefinedhuman.sandboxgameserver.log.Log;

import java.util.LinkedList;

public class ThreadPool extends ThreadGroup {

    private boolean alive;
    private LinkedList<ThreadTask> taskQueue;

    public ThreadPool(TaskType taskType, int numThreads) {

        super("ThreadPool - " + taskType.name());
        setDaemon(true);
        taskQueue = new LinkedList<>();
        alive = true;
        for (int i = 0; i < numThreads; i++) new PooledThread(taskType.name(), this).start();

    }

    public synchronized void addTask(ThreadTask task) {

        if (!alive) throw new IllegalStateException("ThreadManager is dead!");
        if (task != null) {
            taskQueue.add(task);
            notify();
        }

    }

    public synchronized void delete() {

        if (!alive) return;
        alive = false;
        taskQueue.clear();
        interrupt();

    }

    public void join() {

        synchronized (this) {
            alive = false;
            notifyAll();
        }

        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for (int i = 0; i < count; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Log.instance.error(e.getMessage());
            }
        }

    }

    protected synchronized ThreadTask getTask() throws InterruptedException {

        while (taskQueue.size() == 0) {
            if (!alive) return null;
            wait();
        }
        return taskQueue.remove(0);

    }

}
