package de.undefinedhuman.sandboxgame.utils;

public class Timer {

    private float time, delay;
    private boolean loop, finished;
    private Action action;

    public Timer(float delay, boolean loop, Action action) {
        this.time = delay;
        this.delay = delay;
        this.loop = loop;
        this.action = action;
    }

    public void update(float delta) {
        time += delta;
        if (time >= delay && !finished) {
            action.action();
            if (loop) time = 0;
            finished = !loop;
        }
    }

}
