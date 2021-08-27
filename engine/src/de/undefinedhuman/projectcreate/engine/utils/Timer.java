package de.undefinedhuman.projectcreate.engine.utils;

public class Timer {

    private float time = 0, delay;
    private boolean loop, finished;
    private TimerAction timerAction;

    public Timer(float delay, TimerAction timerAction) {
        this(delay, true, timerAction);
    }

    public Timer(float delay, boolean loop, TimerAction timerAction) {
        this.delay = delay;
        this.loop = loop;
        this.timerAction = timerAction;
    }

    public void update(float delta) {
        time += delta;
        if(time < delay || finished)
            return;
        timerAction.action();
        if (loop)
            time = time % delay;
        finished = !loop;
    }

    public void delete() {
        finished = true;
    }

}