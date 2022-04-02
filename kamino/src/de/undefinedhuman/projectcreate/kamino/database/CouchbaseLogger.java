package de.undefinedhuman.projectcreate.kamino.database;

import com.couchbase.client.core.cnc.LoggingEventConsumer;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class CouchbaseLogger implements LoggingEventConsumer.Logger {
    @Override
    public String getName() {
        return "TEST";
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {
        Log.debug(msg);
    }

    @Override
    public void trace(String format, Object... arguments) {

    }

    @Override
    public void trace(String msg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled() {
        return Log.getInstance().getLogLevel().ordinal() >= Level.DEBUG.ordinal();
    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void debug(String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return Log.getInstance().getLogLevel().ordinal() >= Level.INFO.ordinal();
    }

    @Override
    public void info(String msg) {
        Log.info(msg);
    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void info(String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled() {
        return Log.getInstance().getLogLevel().ordinal() >= Level.WARN.ordinal();
    }

    @Override
    public void warn(String msg) {
        Log.warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void warn(String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return Log.getInstance().getLogLevel().ordinal() >= Level.ERROR.ordinal();
    }

    @Override
    public void error(String msg) {
        Log.error(msg);
    }

    @Override
    public void error(String format, Object... arguments) {

    }

    @Override
    public void error(String msg, Throwable t) {

    }
}
