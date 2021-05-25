package de.undefinedhuman.projectcreate.engine.log.decorator;

import de.undefinedhuman.projectcreate.engine.log.Level;

public class LogLevelDecorator extends LogMessageDecorator {

    public LogLevelDecorator(LogMessage logMessage) {
        super(logMessage);
    }

    @Override
    public String createMessage(Level logLevel, String message) {
        return "[" + logLevel.getPrefix() + "]" + super.createMessage(logLevel, message);
    }

}
