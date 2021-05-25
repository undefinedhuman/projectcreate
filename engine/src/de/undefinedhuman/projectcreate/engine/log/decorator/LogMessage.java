package de.undefinedhuman.projectcreate.engine.log.decorator;

import de.undefinedhuman.projectcreate.engine.log.Level;

public interface LogMessage {
    String createMessage(Level logLevel, String message);
}
