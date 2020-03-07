package de.undefinedhuman.sandboxgameserver.utils.threading;

public abstract class ThreadTask implements Runnable {

    @Override
    public void run() {

        runTask();
        endTask();

    }

    public abstract void runTask();

    public abstract void endTask();

}
