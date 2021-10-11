package de.undefinedhuman.projectcreate.engine.utils.timer;

import java.util.ArrayList;
import java.util.Arrays;

public class TimerList {

    private ArrayList<Timer> timers;

    public TimerList() {
        this.timers = new ArrayList<>();
    }

    public void addTimers(Timer... timers) {
        this.timers.addAll(Arrays.asList(timers));
    }

    public void update(float delta) {
        this.timers.forEach(timer -> timer.update(delta));
    }

    public void delete() {
        this.timers.forEach(Timer::delete);
        timers.clear();
    }

}
