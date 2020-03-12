package de.undefinedhuman.sandboxgameserver.utils.threading;

import de.undefinedhuman.sandboxgame.engine.log.Log;

public class PooledThread extends Thread {

    private String name;
    private ThreadPool threadPool;

    public PooledThread(String name, ThreadPool threadPool) {

        this.name = name;
        this.threadPool = threadPool;

    }

    @Override
    public void run() {

        while (!isInterrupted()) {

            ThreadTask task = null;

            try {
                task = threadPool.getTask();
            } catch (InterruptedException ex) {
                Log.error("Thread " + name + " is interrupted:\n" + ex.getMessage());
            }
            if (task == null) return;

            try {
                task.run();
            } catch (Throwable t) {
                threadPool.uncaughtException(this, t);
            }

        }

    }

}
