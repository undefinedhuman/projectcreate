package de.undefinedhuman.sandboxgame.utils;

public abstract class Timer {

    private float time, delay;

    private boolean loop, finished;

    public Timer(float delay, boolean loop) {
        this.time = 0;
        this.delay = delay;
        this.loop = loop;
    }

    public void update(float delta) {

        time += delta;
        if (time >= delay && !finished) {
            action();
            if (loop) time = 0;
            finished = !loop;
        }

    }

    public abstract void action();

}
