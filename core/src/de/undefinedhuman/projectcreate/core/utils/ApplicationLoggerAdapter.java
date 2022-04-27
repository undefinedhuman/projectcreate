package de.undefinedhuman.projectcreate.core.utils;

import com.badlogic.gdx.ApplicationLogger;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class ApplicationLoggerAdapter implements ApplicationLogger {

    private static ApplicationLoggerAdapter instance;

    private ApplicationLoggerAdapter() {}

    @Override
    public void log(String tag, String message) {
        Log.info(message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        Log.error(message, exception);
    }

    @Override
    public void error(String tag, String message) {
        Log.error(message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        Log.error(message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        Log.debug(message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        Log.error(message, exception);
    }

    public static ApplicationLoggerAdapter getInstance() {
        if(instance != null)
            return instance;
        synchronized (ApplicationLoggerAdapter.class) {
            if (instance == null)
                instance = new ApplicationLoggerAdapter();
        }
        return instance;
    }

}
