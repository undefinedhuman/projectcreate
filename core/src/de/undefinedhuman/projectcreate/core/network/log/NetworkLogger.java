package de.undefinedhuman.projectcreate.core.network.log;

import com.esotericsoftware.minlog.Log;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.utils.ds.TriConsumer;

public abstract class NetworkLogger extends Log.Logger {
    @Override
    public void log(int level, String category, String message, Throwable ex) {
        Level logLevel = switch (level) {
            case Log.LEVEL_NONE -> Level.NONE;
            case Log.LEVEL_ERROR -> Level.ERROR;
            case Log.LEVEL_WARN -> Level.WARN;
            case Log.LEVEL_DEBUG, Log.LEVEL_TRACE -> Level.DEBUG;
            default -> Level.INFO;
        };
        log(logLevel, category, message);
    }

    public static void setLogger(Level level, TriConsumer<Level, String, String> log) {
        switch (level) {
            case NONE -> Log.set(Log.LEVEL_NONE);
            case INFO -> Log.set(Log.LEVEL_INFO);
            case ERROR -> Log.set(Log.LEVEL_ERROR);
            case WARN -> Log.set(Log.LEVEL_WARN);
            case DEBUG -> Log.set(Log.LEVEL_DEBUG);
        }
        Log.setLogger(new NetworkLogger() {
            @Override
            public void log(Level level, String category, String message) {
                log.accept(level, category, message);
            }
        });
    }

    public abstract void log(Level level, String category, String message);

}
