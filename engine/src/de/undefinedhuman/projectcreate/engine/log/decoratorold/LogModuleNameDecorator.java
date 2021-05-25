package de.undefinedhuman.projectcreate.engine.log.decoratorold;

import de.undefinedhuman.projectcreate.engine.log.Level;

public class LogModuleNameDecorator extends LogMessageDecorator {

    private final String moduleName;

    public LogModuleNameDecorator(LogMessage logMessage, String moduleName) {
        super(logMessage);
        this.moduleName = moduleName;
    }

    @Override
    public String createMessage(String message) {
        return "[" + moduleName + "]" + message;
    }

}
