package de.undefinedhuman.projectcreate.engine.log.decorator;

import de.undefinedhuman.projectcreate.engine.log.Level;

public class LogModuleNameDecorator extends LogMessageDecorator {

    private String moduleName;

    public LogModuleNameDecorator(LogMessage logMessage, String moduleName) {
        super(logMessage);
        this.moduleName = moduleName;
    }

    @Override
    public String createMessage(Level logLevel, String message) {
        return "[" + moduleName + "]" + super.createMessage(logLevel, message);
    }

}
