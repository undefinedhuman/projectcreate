package de.undefinedhuman.projectcreate.engine.log.decoratorold;

public abstract class LogMessageDecorator implements LogMessage {

    private LogMessage logMessage;

    protected LogMessageDecorator(LogMessage logMessage) {
        this.logMessage = logMessage;
    }
}
