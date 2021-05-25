package de.undefinedhuman.projectcreate.engine.log.decoratorold;

import de.undefinedhuman.projectcreate.engine.log.Level;

public class BaseLogMessage implements LogMessage {
    @Override
    public String createMessage(String message) {
        return message;
    }
}
