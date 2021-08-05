package de.undefinedhuman.projectcreate.core.network.log;

import de.undefinedhuman.projectcreate.engine.log.Log;

public class NetworkLogger extends com.esotericsoftware.minlog.Log.Logger {
    @Override
    public void log(int level, String category, String message, Throwable ex) {
        if(level == com.esotericsoftware.minlog.Log.LEVEL_ERROR && com.esotericsoftware.minlog.Log.ERROR)
            Log.error(message, ex);
        if(level == com.esotericsoftware.minlog.Log.LEVEL_INFO && com.esotericsoftware.minlog.Log.INFO)
            Log.info(message);
        if(level == com.esotericsoftware.minlog.Log.LEVEL_DEBUG && com.esotericsoftware.minlog.Log.DEBUG)
            Log.debug(message);
        if(level == com.esotericsoftware.minlog.Log.LEVEL_TRACE && com.esotericsoftware.minlog.Log.TRACE)
            Log.info(message);
        if(level == com.esotericsoftware.minlog.Log.LEVEL_WARN && com.esotericsoftware.minlog.Log.WARN)
            Log.error(message);
    }
}
