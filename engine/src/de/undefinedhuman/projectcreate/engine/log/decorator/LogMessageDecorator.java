package de.undefinedhuman.projectcreate.engine.log.decorator;

import de.undefinedhuman.projectcreate.engine.log.Level;

public class LogMessageDecorator implements LogMessage {

    private LogMessage logMessage;

    public LogMessageDecorator(LogMessage logMessage) {
        this.logMessage = logMessage;
    }

    @Override
    public String createMessage(Level logLevel, String message) {
        return logMessage.createMessage(logLevel, message);
    }
}
